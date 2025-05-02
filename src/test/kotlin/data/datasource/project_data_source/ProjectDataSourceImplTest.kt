package data.datasource.project_data_source

import FileName
import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.datasource.project_data_source.ProjectDataSourceImpl
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.exceptions.NoStateException
import org.example.models.Project
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID

class ProjectDataSourceImplTest {

    private lateinit var writer: CsvWriter<Project>
    private lateinit var reader: CsvReader<Project>
    private lateinit var dataSource: ProjectDataSource
    private val fileName = FileName.PROJECTS_FILE

    @BeforeEach
    fun setUp() {
        writer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        dataSource = ProjectDataSourceImpl(writer, reader)
    }

    //region Test cases for getProject()
    @Test
    fun `getProject should call read from CsvReader exactly once`() {
        // Given
        val project = createProjectHelper()
        every { reader.read(fileName) } returns listOf(project, createProjectHelper())

        // When
        val result = dataSource.getProject(project.id)

        // Then
        verify(exactly = 1) {
            reader.read(fileName)
        }
    }

    @Test
    fun `getProject should return success with project when project exists`() {
        // Given
        val project = createProjectHelper()
        every { reader.read(fileName) } returns listOf(project, createProjectHelper())

        // When
        val result = dataSource.getProject(project.id)

        // Then
        assertTrue(result.isSuccess)
        val expectedProject = result.getOrNull()
        assertNotNull(expectedProject)
        assertThat(expectedProject?.id).isEqualTo(project.id)
        assertThat(expectedProject?.name).isEqualTo(project.name)
        assertThat(expectedProject?.description).isEqualTo(project.description)
        assertThat(expectedProject?.creatorUserID).isEqualTo(project.creatorUserID)
        assertThat(expectedProject?.createdAt).isEqualTo(project.createdAt)
        assertThat(expectedProject?.updatedAt).isEqualTo(project.updatedAt)
    }

    @Test
    fun `getProject should return failure when repository empty`() {
        // Given
        val project = createProjectHelper()
        every { reader.read(fileName) } returns emptyList()

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
        every { reader.read(fileName) } returns listOf(project, project2)

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
        every { reader.read(fileName) } throws NoProjectFoundException()

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
        every { reader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = createStateHelper(name = "New State")
        val updatedProject = existingProject1.copy(
            state = existingProject1.state + newState, updatedAt = LocalDateTime.now()
        )
        val expectedProjects = listOf(updatedProject, createProjectHelper())
        every { writer.writeToFile(expectedProjects, fileName) } returns Unit

        // When
        val result = dataSource.addStateToProject(id, newState)

        // Then
        verify(exactly = 1) {
            reader.read(fileName)
            writer.writeToFile(any(), fileName)
        }
    }

    @Test
    fun `addStateToProject should return success with Unit when project exists and state is valid`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val existingProject1 = createProjectHelper(id = id)
        val existingProject2 = createProjectHelper()
        every { reader.read(fileName) } returns listOf(existingProject1, existingProject2)
        val newState = createStateHelper(name = "New State")
        val updatedProject = existingProject1.copy(
            state = existingProject1.state + newState, updatedAt = LocalDateTime.now()
        )
        val expectedProjects = listOf(updatedProject, createProjectHelper())
        every { writer.writeToFile(expectedProjects, fileName) } returns Unit

        // When
        val result = dataSource.addStateToProject(id, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(updatedProject.state.size, 2)
        assertEquals(existingProject1.state + newState, updatedProject.state)
    }

    @Test
    fun `addStateToProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        every { reader.read(fileName) } returns listOf(createProjectHelper(), createProjectHelper())
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
        every { reader.read(fileName) } throws IOException("Read failed")
        val state = createStateHelper()

        // When
        val result = dataSource.addStateToProject(id, state)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `addStateToProject should return failure with DuplicateStateException when state name exists`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val existingProject1 = createProjectHelper(id = id, state = listOf(createStateHelper(name = "To Do")))
        val existingProject2 = createProjectHelper()
        every { reader.read(fileName) } returns listOf(existingProject1, existingProject2)
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
        every { reader.read(fileName) } returns listOf(project)
        val newState = oldState.copy(name = "New State")
        val updatedProject = createProjectHelper(id = id, state = listOf(newState))
        every { writer.writeToFile(listOf(updatedProject), fileName) } returns Unit

        // When
        val result = dataSource.editStateToProject(project.id, newState)

        // Then
        verify(exactly = 1) {
            reader.read(fileName)
            writer.writeToFile(any(), fileName)
        }
    }

    @Test
    fun `editStateToProject should return success with Unit when project exists and state is updated`() {
        // Given
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(id = id, state = listOf(oldState))
        every { reader.read(fileName) } returns listOf(project)
        val newState = oldState.copy(name = "New State")
        val updatedProject = createProjectHelper(id = id, state = listOf(newState))
        every { writer.writeToFile(listOf(updatedProject), fileName) } returns Unit

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
        every { reader.read(fileName) } returns listOf(createProjectHelper(), createProjectHelper())
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
        every { reader.read(fileName) } returns listOf(existingProject1, existingProject2)
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
        every { reader.read(fileName) } throws IOException("Read failed")
        val state = createStateHelper()

        // When
        val result = dataSource.editStateToProject(id, state)

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
        every { reader.read(fileName) } returns listOf(existingProject1, existingProject2)
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


}