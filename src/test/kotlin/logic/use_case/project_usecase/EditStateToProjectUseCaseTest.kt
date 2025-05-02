package logic.use_case.project_usecase

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_usecase.EditStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EditStateToProjectUseCaseTest {
    private lateinit var repository: ProjectRepository
    private lateinit var useCase: EditStateToProjectUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = EditStateToProjectUseCase(repository)
    }


    //region Test cases for editStateToProject()
    @Test
    fun `editStateToProject should call addStateToProject from ProjectRepository exactly once`() {
        // Given
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { repository.editStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = useCase.editStateToProject(project.id, newState)

        // Then
        verify(exactly = 1) {
            repository.editStateToProject(project.id, newState)
        }
    }

    @Test
    fun `editStateToProject should return success with Unit when project exists and state is updated`() {
        // Given
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { repository.editStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = useCase.editStateToProject(project.id, newState)

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
        every { repository.editStateToProject(project.id, newState) } returns Result.failure(
            NoProjectFoundException()
        )

        // When
        val result = useCase.editStateToProject(project.id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }

    @Test
    fun `editStateToProject should return failure with DuplicateStateException when state is duplicate`() {
        // Given
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "To Do"
        every { repository.editStateToProject(project.id, newState) } returns Result.failure(
            DuplicateStateException()
        )

        // When
        val result = useCase.editStateToProject(project.id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception?.message)
    }
    //endregion

}