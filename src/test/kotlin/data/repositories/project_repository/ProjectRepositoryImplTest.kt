package data.repositories.project_repository

import CsvParser
import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.data.csv.CsvReader
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.repositories.log_repository.LogRepositoryImpl
import org.example.data.repositories.project_repository.ProjectRepositoryImpl
import org.example.logic.exceptions.CanNotAddOrEditStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.exceptions.NoStateException
import org.example.logic.exceptions.StateHasAssociatedTasksException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProjectRepositoryImplTest {

    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var logRepositoryImpl: LogRepositoryImpl
    private lateinit var reader: CsvReader
    private lateinit var parser: CsvParser

    @BeforeEach
    fun setup() {
        logRepositoryImpl = mockk(relaxed = true)
        projectDataSource = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        parser = mockk(relaxed = true)
        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource, logRepositoryImpl, reader, parser)
    }

    //region Test cases for getProject()
    @Test
    fun `getProject() should call getProject function from ProjectDataSource once`() {
        // Given
        val project = createProjectHelper()

        // When
        val result = projectRepositoryImpl.getProject(project.id)

        // Then
        verify(exactly = 1) {
            projectDataSource.getProject(project.id)
        }
    }

    @Test
    fun `getProject() should return successful result when project exists`() {
        // Given
        val project = createProjectHelper(id = UUID.randomUUID(), creatorUserID = UUID.randomUUID())

        // When
        val result = projectRepositoryImpl.getProject(project.id)

        // Then
        assertTrue(result.isSuccess)
        val expectedProject = result.getOrNull()
        assertNotNull(expectedProject)
        assertThat(expectedProject?.id).isEqualTo(project.id)
    }

    @Test
    fun `getProject() should return failure result when project not found`() {
        // Given
        val project = createProjectHelper(id = UUID.randomUUID())

        // When
        val result = projectRepositoryImpl.getProject(project.id)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `getProject() should return failure result when data source throws exception`() {
        // Given
        val project = createProjectHelper()
        every { projectDataSource.getProject(any()) } throws NoProjectFoundException(project)

        // When
        val result = projectRepositoryImpl.getProject(project.id)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }
    //endregion

    //region Test cases for addStateToProject()
    @Test
    fun `addStateToProject() should call addStateToProject function from ProjectDataSource once`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper(name = "Start")

        // When
        val result = projectRepositoryImpl.addStateToProject(project.id, newState)

        // Then
        verify(exactly = 1) {
            projectDataSource.addStateToProject(project.id, newState)
        }
    }

    @Test
    fun `addStateToProject() should return successful result message when project exists and state is valid`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper(name = "Start")
        every { projectRepositoryImpl.getProject(project.id) } returns Result.success(project)

        // When
        val result = projectRepositoryImpl.addStateToProject(project.id, newState)

        // Then
        assertTrue(result.isSuccess)
        val resultMessage = result.getOrNull()
        assertThat(resultMessage).isEqualTo(StringConstants.Project.ADDED_STATE_SUCCESS)
    }

    @Test
    fun `addStateToProject() should return failure result when project does not exist`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper(name = "Start")
        every { projectRepositoryImpl.getProject(project.id) } returns Result.failure(NoProjectFoundException(project))

        // When
        val result = projectRepositoryImpl.addStateToProject(project.id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `addStateToProject() should return failure result when state is duplicate`() {
        // Given
        val stats = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val projectTesting = createProjectHelper(name = "Test Project", states = stats)
        val projects = listOf(projectTesting, createProjectHelper(), createProjectHelper())
        val duplicateState = createStateHelper(name = "Start")
        every { projectRepositoryImpl.getAllProjects() } returns projects

        // When
        val result = projectRepositoryImpl.addStateToProject(projectTesting.id, duplicateState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is CanNotAddOrEditStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception.message)
    }
    //endregion

    //region Test cases for editStateToProject()
    @Test
    fun `editStateToProject() should call addStateToProject function from ProjectDataSource once`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")

        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, state)

        // Then
        verify(exactly = 1) {
            projectDataSource.editStateToProject(project.id, state)
        }
    }

    @Test
    fun `editStateToProject() should return successful result message when project exists and state is valid`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")
        every { projectRepositoryImpl.getProject(project.id) } returns Result.success(project)


        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, state)

        // Then
        assertTrue(result.isSuccess)
        val resultMessage = result.getOrNull()
        assertThat(resultMessage).isEqualTo(StringConstants.Project.UPDATED_STATE_SUCCESS)
    }

    @Test
    fun `editStateToProject() should return successful result message failure result when project does not exist`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")
        every { projectRepositoryImpl.getProject(project.id) } returns Result.failure(NoProjectFoundException(project))


        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, state)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `editStateToProject() should return failure result when state is duplicate`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")
        every { projectRepositoryImpl.getProject(project.id) } returns Result.success(project)

        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, state)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is CanNotAddOrEditStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception.message)
    }
    //endregion

    //region Test cases for removeStateFromProject()
    @Test
    fun `removeStateFromProject() should call removeStateFromProject function from ProjectDataSource once`() {
        // Given
        val project = createProjectHelper(name = "Test Project")
        val state = createStateHelper(id = project.states.first().id, name = "New Start")

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, state)

        // Then
        verify(exactly = 1) {
            projectDataSource.removeStateFromProject(project.id, state)
        }
    }

    @Test
    fun `removeStateFromProject() should return successful result message when state exists and has no tasks`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")
        every { projectRepositoryImpl.getProject(project.id) } returns Result.success(project)

        every { projectRepositoryImpl.removeStateFromProject(project.id, state) } returns
                Result.success(StringConstants.Project.REMOVED_STATE_SUCCESS)

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, state)

        // Then
        assertTrue(result.isSuccess)
        val resultMessage = result.getOrNull()
        assertThat(resultMessage).isEqualTo(StringConstants.Project.REMOVED_STATE_SUCCESS)
    }

    @Test
    fun `removeStateFromProject() should failure result when project does not exist`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")
        every { projectRepositoryImpl.getProject(project.id) } returns Result.failure(NoProjectFoundException(project))

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, state)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `removeStateFromProject() should failure result when state does not exist`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, state)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoStateException)
        assertEquals(StringConstants.Project.NO_STATE_FOUND, exception.message)
    }

    @Test
    fun `removeStateFromProject() should failure result when state has associated tasks`() {
        // Given
        val states = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(name = "Test Project", states = states)
        val state = createStateHelper(id = project.states.first().id, name = "New Start")

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, state)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is StateHasAssociatedTasksException)
        assertEquals(StringConstants.Project.STATE_HAS_TASKS, exception.message)
    }

    //endregion
}

