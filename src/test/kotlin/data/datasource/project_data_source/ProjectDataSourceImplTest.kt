package data.datasource.project_data_source

import creator_helper.createProjectHelper
import io.mockk.*
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.data.datasource.project_data_source.ProjectDataSourceImpl
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotCreatedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotDeletedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotEditedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotGetAllProjectsException
import org.example.models.Project
import org.junit.jupiter.api.*
import java.io.FileNotFoundException
import java.util.*
import kotlin.test.*
import kotlin.test.Test

class ProjectDataSourceImplTest {
    private lateinit var csvReader: CsvReader<Project>
    private lateinit var csvWriter: CsvWriter<Project>
    private lateinit var dataSource: ProjectDataSourceImpl

    @BeforeEach
    fun setUp() {
        csvReader = mockk(relaxed = true)
        csvWriter = mockk(relaxed = true)
        dataSource = ProjectDataSourceImpl(csvReader, csvWriter)
    }

    @Test
    fun `createProject should write new project to CSV`() {
        // Given
        val testProject = createProjectHelper()
        every { csvReader.read(any()) } returns emptyList()
        every { csvWriter.writeToFile(any(), any()) } returns Result.success(Unit)

        // When
        val result = dataSource.createProject(testProject)

        // Then
        assertTrue(result.isSuccess)
        verify {
            csvWriter.writeToFile(
                match { it.size == 1 }, any()
            )
        }
    }

    @Test
    fun `createProject should fail when project with same name and creator exists`() {
        // Given
        val id = UUID.randomUUID()
        val name = "Z"
        val existingProject = createProjectHelper(creatorUserID = id, name = name)
        val duplicateProject = createProjectHelper(creatorUserID = id, name = name)

        every { csvReader.read(any()) } returns listOf(existingProject)

        // When
        val result = dataSource.createProject(duplicateProject)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ProjectNotCreatedException)
        verify(exactly = 0) { csvWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `createProject should success when project has only duplicated name `() {
        // Given
        val name = "Z"
        val duplicateProject = createProjectHelper(name = name)
        val existingProject = createProjectHelper(name = name)

        every { csvReader.read(any()) } returns listOf(existingProject)

        // When
        val result = dataSource.createProject(duplicateProject)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { csvWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `createProject should success when project has only duplicated id `() {
        // Given
        val name1 = "Z"
        val name2 = "z"
        val duplicateProject = createProjectHelper(name = name1)
        val existingProject = createProjectHelper(name = name2)

        every { csvReader.read(any()) } returns listOf(existingProject)

        // When
        val result = dataSource.createProject(duplicateProject)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { csvWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `createProject should fail when CSV write fails`() {
        // Given
        val testProject = createProjectHelper()
        every { csvReader.read(any()) } returns emptyList()
        every { csvWriter.writeToFile(any(), any()) } throws Exception("Write failed")

        // When
        val result = dataSource.createProject(testProject)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ProjectNotCreatedException)
    }

    @Test
    fun `createProject should propagate getAllProjects failure`() {
        // Arrange
        val testProject = createProjectHelper()
        val expectedError = "Database connection failed"
        every { csvReader.read(any()) } throws Exception(expectedError)

        // Act
        val result = dataSource.createProject(testProject)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(
            "Failed to get projects: $expectedError",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `editProject should propagate getAllProjects failure`() {
        // Arrange
        val testProject = createProjectHelper()
        val expectedError = "Database connection failed"
        every { csvReader.read(any()) } throws Exception(expectedError)

        // Act
        val result = dataSource.editProject(testProject)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(
            "Failed to get projects: $expectedError",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `deleteProject should propagate getAllProjects failure`() {
        // Arrange
        val testProject = createProjectHelper()
        val expectedError = "Database connection failed"
        every { csvReader.read(any()) } throws Exception(expectedError)

        // Act
        val result = dataSource.deleteProject(testProject.id)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(
            "Failed to get projects: $expectedError",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `editProject should update existing project in CSV`() {
        // Given
        val existingProject = createProjectHelper()
        val updatedProject = existingProject.copy(name = "Updated Name")

        every { csvReader.read(any()) } returns listOf(existingProject)
        every { csvWriter.writeToFile(any(), any()) } returns Result.success(Unit)

        // When
        val result = dataSource.editProject(updatedProject)

        // Then
        assertTrue(result.isSuccess)
        verify {
            csvWriter.writeToFile(
                match { projects ->
                    projects.size == 1 &&
                            projects[0].id == updatedProject.id &&
                            projects[0].name == "Updated Name" &&
                            projects[0].updatedAt.isAfter(existingProject.updatedAt)
                },
                any()
            )
        }
    }

    @Test
    fun `editProject should fail when project not found`() {
        // Given
        val nonExistentProject = createProjectHelper()
        every { csvReader.read(any()) } returns emptyList()

        // When
        val result = dataSource.editProject(nonExistentProject)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ProjectNotEditedException)
        verify(exactly = 0) { csvWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `editProject should fail when CSV write fails`() {
        // Given
        val existingProject = createProjectHelper()
        val updatedProject = existingProject.copy(name = "Updated")

        every { csvReader.read(any()) } returns listOf(existingProject)
        every { csvWriter.writeToFile(any(), any()) } throws Exception("Write failed")

        // When
        val result = dataSource.editProject(updatedProject)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ProjectNotEditedException)
    }


    @Test
    fun `deleteProject should remove project from CSV`() {
        // Given
        val projectId = UUID.randomUUID()
        val projectToDelete = createProjectHelper(id = projectId)
        val projectToKeep = createProjectHelper()
        every { csvReader.read(any()) } returns listOf(projectToDelete, projectToKeep)
        every { csvWriter.writeToFile(any(), any()) } returns Result.success(Unit)

        // When
        val result = dataSource.deleteProject(projectId)

        // Then
        assertTrue(result.isSuccess)
        verify { csvWriter.writeToFile(listOf(projectToKeep), any()) }
    }

    @Test
    fun `deleteProject should fail when project not found`() {
        // Given
        val nonExistentId = UUID.randomUUID()
        val exception = ProjectNotGetAllProjectsException("CSV read error")
        every { csvReader.read(any()) } throws exception
        every { csvReader.read(any()) } returns emptyList()


        // When
        val result = dataSource.deleteProject(nonExistentId)

        // Then
        assertTrue(result.isFailure)
        assertIs<ProjectNotDeletedException>(result.exceptionOrNull())
        verify(exactly = 0) { csvWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `deleteProject should fail when CSV write fails`() {
        // Given
        val projectId = UUID.randomUUID()
        val projectToDelete = createProjectHelper(id = projectId)

        every { csvReader.read(any()) } returns listOf(projectToDelete)
        every { csvWriter.writeToFile(any(), any()) } throws Exception("Write failed")

        // When
        val result = dataSource.deleteProject(projectId)

        // Then
        assertTrue(result.isFailure)
        assertIs<ProjectNotDeletedException>(result.exceptionOrNull())
    }

    @Test
    fun `getAllProjects should return projects from CSV`() {
        // Given
        val expectedProjects = listOf(
            createProjectHelper(name = "Project 1"),
            createProjectHelper(name = "Project 2")
        )
        every { csvReader.read(any()) } returns expectedProjects

        // When
        val result = dataSource.getAllProjects()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedProjects, result.getOrNull())
    }

    @Test
    fun `getAllProjects should fail when CSV read fails`() {
        // Given
        val exception = ProjectNotGetAllProjectsException("CSV read error")
        every { csvReader.read(any()) } throws exception

        // When
        val result = dataSource.getAllProjects()

        // Then
        assertTrue(result.isFailure)
        assertIs<ProjectNotGetAllProjectsException>(result.exceptionOrNull())
    }

    @Test
    fun `getAllProjects should return empty list when file not found`() {
        // Given
        every { csvReader.read(any()) } throws FileNotFoundException("File not found")

        // When
        val result = dataSource.getAllProjects()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(emptyList<Project>(), result.getOrNull())
    }
        @Test
        fun `buildSuccessCreate should return success when project is unique`() {
            // Given
            val testProject = createProjectHelper()
            every { csvReader.read(any()) } returns emptyList()
            every { csvWriter.writeToFile(any(), any()) } returns Result.success(Unit)

            // When
            val result = dataSource.createProject(testProject)

            // Then
            assertTrue(result.isSuccess)
            verify {
                csvWriter.writeToFile(
                    match { it.size == 1 && it[0].name == testProject.name },
                    any()
                )
            }
        }

        @Test
        fun `buildSuccessCreate should fail when project exists with same name and creator`() {
            // Given
            val creatorId = UUID.randomUUID()
            val projectName = "Existing Project"
            val existingProject = createProjectHelper(name = projectName, creatorUserID = creatorId)
            val newProject = createProjectHelper(name = projectName, creatorUserID = creatorId)

            every { csvReader.read(any()) } returns listOf(existingProject)

            // When
            val result = dataSource.createProject(newProject)

            // Then
            assertTrue(result.isFailure)
            assertIs<ProjectNotCreatedException>(result.exceptionOrNull())
            assertEquals(
                "Project '$projectName' already exists for user $creatorId",
                result.exceptionOrNull()?.message
            )
        }

        @Test
        fun `buildSuccessEditor should update project successfully`() {
            // Given
            val existingProject = createProjectHelper()
            val updatedProject = existingProject.copy(name = "Updated Name")

            every { csvReader.read(any()) } returns listOf(existingProject)
            every { csvWriter.writeToFile(any(), any()) } returns Result.success(Unit)

            // When
            val result = dataSource.editProject(updatedProject)

            // Then
            assertTrue(result.isSuccess)
            verify {
                csvWriter.writeToFile(
                    match { projects ->
                        projects.size == 1 &&
                                projects[0].id == updatedProject.id &&
                                projects[0].name == "Updated Name"
                    },
                    any()
                )
            }
        }

        @Test
        fun `buildSuccessDelete should remove project successfully`() {
            // Given
            val projectId = UUID.randomUUID()
            val projectToDelete = createProjectHelper(id = projectId)
            val projectToKeep = createProjectHelper()

            every { csvReader.read(any()) } returns listOf(projectToDelete, projectToKeep)
            every { csvWriter.writeToFile(any(), any()) } returns Result.success(Unit)

            // When
            val result = dataSource.deleteProject(projectId)

            // Then
            assertTrue(result.isSuccess)
            verify { csvWriter.writeToFile(listOf(projectToKeep), any()) }
        }

}