package domain.use_case.state

import creator_helper.*
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.UUID

class EditStateUseCaseTest {

    private val editProjectStateUseCase: EditProjectStateUseCase = mockk(relaxed = true)
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk(relaxed = true)
    private lateinit var editStateUseCase: EditStateUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        editStateUseCase = EditStateUseCase(editProjectStateUseCase, getCurrentUserUseCase)
    }
    @Test
    fun `editState updates state name and returns updated project`() = runTest {
        // Given
        val currentUser = createUserHelper()

        val stateId1 = UUID.randomUUID()
        val stateId2 = UUID.randomUUID()

        val testState = createStateHelper(
            id = stateId1,
            name = "Original State Name"
        )

        val testState2 = createStateHelper(
            id = stateId2,
            name = "Another State"
        )

        val testProject = createProjectHelper(
            id = UUID.randomUUID(),
            name = "Test Project",
            description = "Test Description",
            creatorUserID = currentUser.id,
            state = listOf(testState, testState2)
        )

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns currentUser
        val newStateName = "Updated State Name"

        // When
        val result = editStateUseCase.editState(testState, newStateName, testProject)

        // Then
        assertEquals(2, result.state.size)
        val updatedState = result.state.find { it.id == stateId1 }
        val unchangedState = result.state.find { it.id == stateId2 }
        assertEquals(newStateName, updatedState?.name)
        assertEquals(testState2.name, unchangedState?.name)

        coVerify {
            editProjectStateUseCase.editStateToProject(
                currentUser.id,
                testProject,
                withArg { state ->
                    assertEquals(stateId1, state.id)
                    assertEquals(newStateName, state.name)
                }
            )
        }
    }

    @Test
    fun `editState throws exception when name is empty`() = runTest {
        // Given
        val currentUserId = UUID.randomUUID()
        val currentUser = createUserHelper(
            id = currentUserId,
            name = "Test User",
            email = "test@example.com"
        )

        val stateId = UUID.randomUUID()
        val testState = createStateHelper(
            id = stateId,
            name = "Original State Name"
        )

        val testProject = createProjectHelper(
            id = UUID.randomUUID(),
            name = "Test Project",
            description = "Test Description",
            creatorUserID = currentUserId,
            state = listOf(testState)
        )

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns currentUser
        val emptyName = ""

        // When/Then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                editStateUseCase.editState(testState, emptyName, testProject)
            }
        }

        assertEquals("Edit failed: name cannot be blank!", exception.message)
    }

    @Test
    fun `editState throws exception when user is not logged in`() = runTest {
        // Given
        val stateId = UUID.randomUUID()
        val testState = createStateHelper(
            id = stateId,
            name = "Original State Name"
        )

        val testProject = createProjectHelper(
            id = UUID.randomUUID(),
            name = "Test Project",
            description = "Test Description",
            creatorUserID = UUID.randomUUID(),
            state = listOf(testState)
        )

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns null
        val newStateName = "Updated State Name"

        // When/Then
        val exception = assertThrows(Exception::class.java) {
            runBlocking {
                editStateUseCase.editState(testState, newStateName, testProject)
            }
        }

        assertEquals("User not logged in", exception.message)
    }



}