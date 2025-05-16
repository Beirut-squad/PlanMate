package domain.useCase.state

import creator_helper.*
import domain.useCase.authentication.GetCurrentUserUseCase
import domain.useCase.project.DeleteProjectStateUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

class DeleteTaskStateUseCaseTest {
    private val deleteProjectStateUseCase: DeleteProjectStateUseCase = mockk(relaxed = true)
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk(relaxed = true)
    private lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        deleteStateUseCase = DeleteStateUseCase(deleteProjectStateUseCase, getCurrentUserUseCase)
    }

    @Test
    fun `deleteState returns updated project when current user exists`() = runTest {
        // Given
        val user = createUserHelper()
        val project = createProjectHelper()
        val state = createStateHelper()
        val updatedProject = project.copy(taskStates = project.taskStates - state)

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery {
            deleteProjectStateUseCase.removeStateFromProject(user.id, project, state)
        } returns updatedProject

        // When
        val result = deleteStateUseCase.deleteState(project, state)

        // Then
        assertEquals(updatedProject, result)
        coVerifyOrder {
            getCurrentUserUseCase.getCurrentUser()
            deleteProjectStateUseCase.removeStateFromProject(user.id, project, state)
        }
    }



}