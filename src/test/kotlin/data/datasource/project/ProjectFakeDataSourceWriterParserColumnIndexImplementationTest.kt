package data.datasource.project

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import org.example.data.csv.helper.FileName
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.ui.constants.StringConstants
import org.example.data.csv.reader.CsvReader
import org.example.data.csv.writer.CsvWriter
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotCreatedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotDeletedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotEditedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotGetAllProjectsException
import org.example.logic.exceptions.project_magement_exceptions.DuplicateStateException
import org.example.logic.exceptions.project_magement_exceptions.NoProjectFoundException
import org.example.logic.exceptions.project_magement_exceptions.NoStateException
import org.example.data.model.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID
import java.io.FileNotFoundException
import kotlin.test.*
import kotlin.test.Test

class ProjectFakeDataSourceWriterParserColumnIndexImplementationTest {
    private lateinit var csvReader: CsvReader<Project>
    private lateinit var csvWriter: CsvWriter<Project>
    private lateinit var dataSource: ProjectDataSourceImplementation
    private val fileName = FileName.PROJECTS

    @BeforeEach
    fun setUp() {
        csvReader = mockk(relaxed = true)
        csvWriter = mockk(relaxed = true)
        dataSource = ProjectDataSourceImplementation(csvReader, csvWriter)
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
        val updatedProject = existingProject.copy(title = "Updated Name")

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
                            projects[0].title == "Updated Name" &&
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
        val updatedProject = existingProject.copy(title = "Updated")

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
                    match { it.size == 1 && it[0].title == testProject.title },
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
            val updatedProject = existingProject.copy(title = "Updated Name")

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
                                projects[0].title == "Updated Name"
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
    //region Test cases for getProject()
    @Test
    fun `getProject should call read from CsvReader exactly once`() {
        // Given
        val project = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(project, createProjectHelper())

        // When
        val result = dataSource.getProject(project.id)

        // Then
        verify(exactly = 1) {
            csvReader.read(fileName)
        }
    }

    @Test
    fun `getProject should return success with project when project exists`() {
        // Given
        val project = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(project, createProjectHelper())

        // When
        val result = dataSource.getProject(project.id)

        // Then
        assertTrue(result.isSuccess)
        val expectedProject = result.getOrNull()
        assertNotNull(expectedProject)
        assertThat(expectedProject?.id).isEqualTo(project.id)
        assertThat(expectedProject?.name).isEqualTo(project.title)
        assertThat(expectedProject?.description).isEqualTo(project.description)
        assertThat(expectedProject?.creatorUserID).isEqualTo(project.creatorUserID)
        assertThat(expectedProject?.createdAt).isEqualTo(project.createdAt)
        assertThat(expectedProject?.updatedAt).isEqualTo(project.updatedAt)
    }

    @Test
    fun `getProject should return failure when repository empty`() {
        // Given
        val project = createProjectHelper()
        every { csvReader.read(fileName) } returns emptyList()

        // When
        val result = dataSource.getProject(project.id)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoProjectFoundException)
    }

    @Test
    fun `getProject should handle multiple projects correctly`() {
        // Given
        val project = createProjectHelper()
        val project2 = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(project, project2)

        // When
        val result = dataSource.getProject(project.id)
        val result2 = dataSource.getProject(project2.id)

        // Then
        assertEquals(project, result.getOrNull())
        assertEquals(project2, result2.getOrNull())
    }


    @Test
    fun `getProject() should return failure with NoProjectFoundException when project not found`() {
        // Given
        val project = createProjectHelper()
        every { csvReader.read(fileName) } throws NoProjectFoundException()

        // When
        val result = dataSource.getProject(project.id)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }
    //endregion

    //region Test cases for addStateToProject()
    @Test
    fun `addStateToProject should call writeToFile form csv writer and read from csv reader exactly once`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val existingProject1 = createProjectHelper(id = id)
        val existingProject2 = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = createStateHelper(name = "New State")
        val updatedProject = existingProject1.copy(
            state = existingProject1.state + newState, updatedAt = LocalDateTime.now()
        )
        val expectedProjects = listOf(updatedProject, createProjectHelper())
        every { csvWriter.writeToFile(expectedProjects, fileName) } returns Result.success(Unit)

        // When
        val result = dataSource.addStateToProject(id, newState)

        // Then
        verify(exactly = 1) {
            csvReader.read(fileName)
            csvWriter.writeToFile(any(), fileName)
        }
    }

    @Test
    fun `addStateToProject should return success with Unit when project exists and state is valid`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val existingProject1 = createProjectHelper(id = id)
        val existingProject2 = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = createStateHelper(name = "New State")
        val updatedProject = existingProject1.copy(
            state = existingProject1.state + newState, updatedAt = LocalDateTime.now()
        )
        val expectedProjects = listOf(updatedProject, createProjectHelper())
        every { csvWriter.writeToFile(expectedProjects, fileName) } returns Result.success(Unit)

        // When
        val result = dataSource.addStateToProject(id, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(updatedProject.state.size, 2)
        assertEquals(existingProject1.state + newState, updatedProject.state)
    }

    @Test
    fun `addStateToProject should fail when CSV write fails`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val existingProject1 = createProjectHelper(id = id)
        val existingProject2 = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = createStateHelper(name = "New State")
        val updatedProject = existingProject1.copy(
            state = existingProject1.state + newState, updatedAt = LocalDateTime.now()
        )
        every { csvWriter.writeToFile(any(), fileName) } throws IOException("Write failed")

        // When
        val result = dataSource.addStateToProject(id, newState)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `addStateToProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        every { csvReader.read(fileName) } returns listOf(createProjectHelper(), createProjectHelper())
        val newState = createStateHelper(name = "New State")

        // When
        val result = dataSource.addStateToProject(id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }

    @Test
    fun `addStateToProject should propagate IOException when file read fails`() {
        // Given
        val id = UUID.randomUUID()
        every { csvReader.read(fileName) } throws IOException("Read failed")
        val state = createStateHelper()

        // When
        val result = dataSource.addStateToProject(id, state)

        // Then
        assertTrue(result.isFailure)
        assertThat(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `addStateToProject should return failure with DuplicateStateException when state name exists`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val existingProject1 = createProjectHelper(id = id, state = listOf(createStateHelper(name = "To Do")))
        val existingProject2 = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = createStateHelper(name = "to do")

        // When
        val result = dataSource.addStateToProject(id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception?.message)
    }
    //endregion


    //region Test cases for editStateToProject()
    @Test
    fun `editStateToProject should call writeToFile form csv writer and read from csv reader exactly once`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(id = id, state = listOf(oldState))
        every { csvReader.read(fileName) } returns listOf(project)
        val newState = oldState.copy(name = "New State")
        val updatedProject = createProjectHelper(id = id, state = listOf(newState))
        every { csvWriter.writeToFile(listOf(updatedProject), fileName) } returns Result.success(Unit)

        // When
        val result = dataSource.editStateToProject(project.id, newState)

        // Then
        verify(exactly = 1) {
            csvReader.read(fileName)
            csvWriter.writeToFile(any(), fileName)
        }
    }

    @Test
    fun `editStateToProject should return success with Unit when project exists and state is updated`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(id = id, state = listOf(oldState))
        every { csvReader.read(fileName) } returns listOf(project)
        val newState = oldState.copy(name = "New State")
        val updatedProject = createProjectHelper(id = id, state = listOf(newState))
        every { csvWriter.writeToFile(listOf(updatedProject), fileName) } returns Result.success(Unit)

        // When
        val result = dataSource.editStateToProject(project.id, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(updatedProject.state.size, 1)
        assertEquals(listOf(newState), updatedProject.state)
    }

    @Test
    fun `editStateToProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        every { csvReader.read(fileName) } returns listOf(createProjectHelper(), createProjectHelper())
        val newState = createStateHelper(name = "New State")

        // When
        val result = dataSource.editStateToProject(id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }

    @Test
    fun `editStateToProject should return failure with NoStateException when state does not exist`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val oldState = createStateHelper(name = "To Do")
        val existingProject1 = createProjectHelper(id = id, state = listOf(oldState))
        val existingProject2 = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = createStateHelper(name = "New State")

        // When
        val result = dataSource.editStateToProject(id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoStateException)
        assertEquals(StringConstants.Project.NO_STATE_FOUND, exception?.message)
    }

    @Test
    fun `editStateToProject should propagate IOException when file read fails`() {
        // Given
        val id = UUID.randomUUID()
        every { csvReader.read(fileName) } throws IOException("Read failed")
        val state = createStateHelper()

        // When
        val result = dataSource.editStateToProject(id, state)

        // Then
        assertTrue(result.isFailure)
        assertThat(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `editStateToProject should fail when CSV write fails`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(id = id, state = listOf(oldState))
        every { csvReader.read(fileName) } returns listOf(project)
        val newState = oldState.copy(name = "New State")
        every { csvWriter.writeToFile(any(), fileName) } throws IOException("Write failed")

        // When
        val result = dataSource.editStateToProject(id, newState)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `editStateToProject should return failure with DuplicateStateException when state name exists`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val oldState = createStateHelper(name = "To Do")
        val existingProject1 = createProjectHelper(id = id, state = listOf(oldState))
        val existingProject2 = createProjectHelper()
        every { csvReader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = oldState.copy(name = "to do")

        // When
        val result = dataSource.editStateToProject(id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception?.message)
    }
    //endregion

    //region Test cases for removeStateToProject()
    @Test
    fun `removeStateToProject should call writeToFile form csv writer and read from csv reader exactly once`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val removeState = createStateHelper(name = "To Do")
        val project = createProjectHelper(id = id, state = listOf(removeState))
        every { csvReader.read(fileName) } returns listOf(project)
        val updatedProject = createProjectHelper(id = id, state = emptyList())
        every { csvWriter.writeToFile(listOf(updatedProject), fileName) } returns Result.success(Unit)

        // When
        val result = dataSource.removeStateFromProject(project.id, removeState)

        // Then
        verify(exactly = 1) {
            csvReader.read(fileName)
            csvWriter.writeToFile(any(), fileName)
        }
    }

    @Test
    fun `removeStateToProject should return success with Unit when project exists and state is removed`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val removeState = createStateHelper(name = "To Do")
        val project = createProjectHelper(id = id, state = listOf(removeState))
        every { csvReader.read(fileName) } returns listOf(project)
        val updatedProject = createProjectHelper(id = id, state = emptyList())
        every { csvWriter.writeToFile(listOf(updatedProject), fileName) } returns Result.success(Unit)

        // When
        val result = dataSource.removeStateFromProject(project.id, removeState)

        //Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertTrue(updatedProject.state.isEmpty())
    }

    @Test
    fun `removeStateFromProject should return failure with NoStateException when state not found`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val removeState = createStateHelper(name = "To Do")
        val project = createProjectHelper(id = id, state = listOf(createStateHelper()))
        every { csvReader.read(fileName) } returns listOf(project)

        // When
        val result = dataSource.removeStateFromProject(project.id, removeState)

        //Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoStateException)
        assertEquals(StringConstants.Project.NO_STATE_FOUND, exception?.message)
    }

    @Test
    fun `removeStateFromProject should propagate IOException when file read fails`() {
        // Given
        val id = UUID.randomUUID()
        every { csvReader.read(fileName) } throws IOException("Read failed")
        val state = createStateHelper()

        // When
        val result = dataSource.removeStateFromProject(id, state)

        // Then
        assertTrue(result.isFailure)
        assertThat(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `removeStateFromProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        every { csvReader.read(fileName) } returns listOf(createProjectHelper(), createProjectHelper())
        val state = createStateHelper(name = "New State")

        // When
        val result = dataSource.removeStateFromProject(id, state)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }

    @Test
    fun `removeStateFromProject should fail when CSV write fails`() {
        // Given
        val projectId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val state = createStateHelper()
        val project = createProjectHelper(id = projectId, state = listOf(state))
        every { csvReader.read(fileName) } returns listOf(project)
        every { csvWriter.writeToFile(any(), fileName) } throws IOException("Write failed")

        // When
        val result = dataSource.removeStateFromProject(projectId, state)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }
    //endregion

    @Test
    fun `getProjectsForUserById returns projects when user has projects`() {
        // Given
        val userId = UUID.randomUUID()
        val projects = listOf(
            Project(UUID.randomUUID(), "Project 1", "Desc", userId, state = listOf()),
            Project(UUID.randomUUID(), "Project 2", "Desc", userId, state = listOf())
        )
        every { csvReader.read(fileName) } returns projects

        // When
        val result = dataSource.getProjectsForUserById(userId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals(userId, result.getOrNull()?.first()?.creatorUserID)
    }
    @Test
    fun `getProjectsForUserById returns empty list when user has no projects`() {
        // Given
        val userId = UUID.randomUUID()
        val otherUserId = UUID.randomUUID()
        val projects = listOf(
            Project(UUID.randomUUID(), "Other Project", "Desc", otherUserId, state = listOf())
        )
        every { csvReader.read(fileName) } returns projects

        // When
        val result = dataSource.getProjectsForUserById(userId)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
    @Test
    fun `getProjectsForUserById returns failure when exception occurs`() {
        // Given
        val userId = UUID.randomUUID()
        every { csvReader.read(fileName) } throws RuntimeException("File read error")

        // When
        val result = dataSource.getProjectsForUserById(userId)

        // Then
        assertTrue(result.isFailure)
        assertEquals("File read error", result.exceptionOrNull()?.message)
    }

}