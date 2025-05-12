package domain.use_case.state

import creator_helper.*
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.DeleteProjectStateUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

class DeleteStateUseCaseTest {
    private val deleteProjectStateUseCase: DeleteProjectStateUseCase = mockk(relaxed = true)
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk(relaxed = true)
    private lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        deleteStateUseCase = DeleteStateUseCase(deleteProjectStateUseCase, getCurrentUserUseCase)
    }

    @Test
    fun `deleteState should return updated project when user exists`() = runTest {
        // Given
        val user = createUserHelper()
        val project = createProjectHelper()
        val state = createStateHelper()
        val updatedProject = project.copy(state = project.state - state)

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { deleteProjectStateUseCase.removeStateFromProject(user.id, project, state) } returns updatedProject

        // When
        val result = deleteStateUseCase.deleteState(project, state)

        // Then
        assertEquals(updatedProject, result)
        coVerify(exactly = 1) { getCurrentUserUseCase.getCurrentUser() }
        coVerify(exactly = 1) { deleteProjectStateUseCase.removeStateFromProject(user.id, project, state) }
    }

    @Test
    fun `deleteState should throw IllegalArgumentException when current user is null`() = runTest {
        // Given
        val project = createProjectHelper()
        val state = createStateHelper()

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns null

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            deleteStateUseCase.deleteState(project, state)
        }

        assertNotNull(exception)
        coVerify(exactly = 1) { getCurrentUserUseCase.getCurrentUser() }
        coVerify { deleteProjectStateUseCase wasNot Called }
    }
    @Test
    fun `deleteState should proceed when current user exists`() = runTest {
        // Given
        val user = createUserHelper()
        val project = createProjectHelper()
        val state = createStateHelper()
        val updatedProject = project.copy(state = project.state - state)

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { deleteProjectStateUseCase.removeStateFromProject(user.id, project, state) } returns updatedProject

        // When
        val result = deleteStateUseCase.deleteState(project, state)

        // Then
        assertEquals(updatedProject, result)
        coVerify(exactly = 1) { getCurrentUserUseCase.getCurrentUser() }
        coVerify(exactly = 1) { deleteProjectStateUseCase.removeStateFromProject(user.id, project, state) }
    }



}