package data.repositories.project_repository

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.repositories.log_repository.LogRepositoryImpl
import org.example.data.repositories.project_repository.ProjectRepositoryImpl
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.exceptions.NoStateException
import org.example.logic.exceptions.StateHasAssociatedTasksException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProjectRepositoryImplTest {

    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var logRepositoryImpl: LogRepositoryImpl

    @BeforeEach
    fun setup() {
        logRepositoryImpl = mockk(relaxed = true)
        projectDataSource = mockk(relaxed = true)
        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource, logRepositoryImpl)
    }

    //region Test cases for getProject()
    @Test
    fun `getProject should call getProject from ProjectDataSource exactly once`() {
        // Given
        val project = createProjectHelper()
        every { projectDataSource.getProject(project.id) } returns Result.success(project)

        // When
        val result = projectRepositoryImpl.getProject(project.id)

        // Then
        verify(exactly = 1) {
            projectDataSource.getProject(project.id)
        }
    }

    @Test
    fun `getProject should return success with project when project exists`() {
        // Given
        val project = createProjectHelper()
        every { projectDataSource.getProject(project.id) } returns Result.success(project)

        // When
        val result = projectRepositoryImpl.getProject(project.id)

        // Then
        assertTrue(result.isSuccess)
        val expectedProject = result.getOrNull()
        assertNotNull(expectedProject)
        assertThat(expectedProject.id).isEqualTo(project.id)
    }

    @Test
    fun `getProject() should return failure with NoProjectFoundException when project not found`() {
        // Given
        val project = createProjectHelper()
        every { projectDataSource.getProject(project.id) } returns Result.failure(NoProjectFoundException())

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
    fun `addStateToProject should call addStateToProject from ProjectDataSource exactly once`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper(name = "Start")
        every { projectDataSource.addStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = projectRepositoryImpl.addStateToProject(project.id, newState)

        // Then
        verify(exactly = 1) {
            projectDataSource.addStateToProject(project.id, newState)
        }
    }

    @Test
    fun `addStateToProject() should return success with Unit when project exists and state is valid`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { projectDataSource.addStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = projectRepositoryImpl.addStateToProject(project.id, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `addStateToProject() should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { projectDataSource.addStateToProject(project.id, newState) } returns Result.failure(
            NoProjectFoundException()
        )

        // When
        val result = projectRepositoryImpl.addStateToProject(project.id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `addStateToProject() should return failure with DuplicateStateException when state name exists`() {
        // Given
        val stats = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(state = stats)
        val duplicateState = createStateHelper(name = "Start")
        every { projectDataSource.addStateToProject(project.id, duplicateState) } returns Result.failure(
            DuplicateStateException()
        )

        // When
        val result = projectRepositoryImpl.addStateToProject(project.id, duplicateState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception.message)
    }
    //endregion

    //region Test cases for editStateToProject()
    @Test
    fun `editStateToProject should call addStateToProject from ProjectDataSource exactly once`() {
        // Given
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { projectDataSource.editStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, newState)

        // Then
        verify(exactly = 1) {
            projectDataSource.editStateToProject(project.id, newState)
        }
    }

    @Test
    fun `editStateToProject should return success with Unit when project exists and state is updated`() {
        // Given
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { projectDataSource.editStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `editStateToProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { projectDataSource.editStateToProject(project.id, newState) } returns Result.failure(NoProjectFoundException())


        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `editStateToProject should return failure with DuplicateStateException when state is duplicate`() {
        // Given
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "To Do"
        every { projectDataSource.editStateToProject(project.id, newState) } returns Result.failure(DuplicateStateException())

        // When
        val result = projectRepositoryImpl.editStateToProject(project.id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception.message)
    }
    //endregion

    //region Test cases for removeStateFromProject()
    @Test
    fun `removeStateFromProject should call removeStateFromProject from ProjectDataSource exactly once`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { projectDataSource.removeStateFromProject(project.id, removeState) } returns Result.success(Unit)

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, removeState)

        // Then
        verify(exactly = 1) {
            projectDataSource.removeStateFromProject(project.id, removeState)
        }
    }

    @Test
    fun `removeStateFromProject should return success with Unit when state exists and has no tasks`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { projectDataSource.removeStateFromProject(project.id, removeState) } returns Result.success(Unit)

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `removeStateFromProject() should failure with NoProjectFoundException when project does not exist`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { projectDataSource.removeStateFromProject(project.id, removeState) } returns Result.failure(NoProjectFoundException())

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `removeStateFromProject should failure with NoStateException when state does not exist`() {
        // Given
        val removeState = createStateHelper(id = UUID.randomUUID(), name = "Undo")
        val project = createProjectHelper()
        every { projectDataSource.removeStateFromProject(project.id, removeState) } returns Result.failure(NoStateException())

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoStateException)
        assertEquals(StringConstants.Project.NO_STATE_FOUND, exception.message)
    }

    @Test
    fun `removeStateFromProject should failure with StateHasAssociatedTasksException when state has associated with tasks`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { projectDataSource.removeStateFromProject(project.id, removeState) } returns Result.failure(StateHasAssociatedTasksException())

        // When
        val result = projectRepositoryImpl.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is StateHasAssociatedTasksException)
        assertEquals(StringConstants.Project.STATE_HAS_TASKS, exception.message)
    }
    //endregion
}

