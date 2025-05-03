package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.logic.exceptions.project_magement_exceptions.NoProjectFoundException
import org.example.logic.exceptions.project_magement_exceptions.NoStateException
import org.example.logic.exceptions.project_magement_exceptions.StateHasAssociatedTasksException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.project_manegment.RemoveStateFromProjectUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class RemoveStateFromProjectUseCaseTest {
    private lateinit var repository: ProjectRepository
    private lateinit var useCase: RemoveStateFromProjectUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = RemoveStateFromProjectUseCase(repository)
    }

    //region Test cases for removeStateFromProject()
    @Test
    fun `removeStateFromProject should call removeStateFromProject from ProjectRepository exactly once`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { repository.removeStateFromProject(project.id, removeState) } returns Result.success(Unit)

        // When
        val result = useCase.removeStateFromProject(project.id, removeState)

        // Then
        verify(exactly = 1) {
            repository.removeStateFromProject(project.id, removeState)
        }
    }

    @Test
    fun `removeStateFromProject should return success with Unit when state exists and has no tasks`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { repository.removeStateFromProject(project.id, removeState) } returns Result.success(Unit)

        // When
        val result = useCase.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `removeStateFromProject should failure with NoProjectFoundException when project does not exist`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { repository.removeStateFromProject(project.id, removeState) } returns Result.failure(
            NoProjectFoundException()
        )

        // When
        val result = useCase.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }

    @Test
    fun `removeStateFromProject should failure with NoStateException when state does not exist`() {
        // Given
        val removeState = createStateHelper(id = UUID.randomUUID(), name = "Undo")
        val project = createProjectHelper()
        every { repository.removeStateFromProject(project.id, removeState) } returns Result.failure(
            NoStateException()
        )

        // When
        val result = useCase.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoStateException)
        assertEquals(StringConstants.Project.NO_STATE_FOUND, exception?.message)
    }

    @Test
    fun `removeStateFromProject should failure with StateHasAssociatedTasksException when state has associated with tasks`() {
        // Given
        val removeState = createStateHelper()
        val project = createProjectHelper(state = listOf(removeState))
        every { repository.removeStateFromProject(project.id, removeState) } returns Result.failure(
            StateHasAssociatedTasksException()
        )

        // When
        val result = useCase.removeStateFromProject(project.id, removeState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is StateHasAssociatedTasksException)
        assertEquals(StringConstants.Project.STATE_HAS_TASKS, exception?.message)
    }
    //endregion
}