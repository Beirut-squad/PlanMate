package domain.use_case.state

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import ui.common.exception.EmptyStateNameException
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.EditProjectStateUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

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
    fun `editState should updates the correct state and returns updated project`() = runTest {
        // Given
        val currentUser = createUserHelper()

        val targetStateId = UUID.randomUUID()
        val otherStateId = UUID.randomUUID()

        val targetState = createStateHelper(id = targetStateId, name = "To Do")
        val otherState = createStateHelper(id = otherStateId, name = "In Progress")

        val project = createProjectHelper(
            creatorUserID = currentUser.id,
            state = listOf(targetState, otherState)
        )

        val newStateName = "Updated State"

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns currentUser

        // When
        val result = editStateUseCase.editState(targetState, newStateName, project)

        // Then
        assertEquals(2, result.states.size)

        val updatedState = result.states.find { it.id == targetStateId }
        val unchangedState = result.states.find { it.id == otherStateId }

        assertNotNull(updatedState)
        assertEquals(newStateName, updatedState?.name)

        assertNotNull(unchangedState)
        assertEquals("In Progress", unchangedState?.name)

        coVerify(exactly = 1) {
            editProjectStateUseCase.editStateToProject(
                currentUser.id,
                project,
                match { it.id == targetStateId && it.name == newStateName }
            )
        }
    }

    @Test
    fun `editState should throws EmptyStateNameException when new name is blank`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        // When & Then
        assertThrows<EmptyStateNameException> {
            runBlocking {
                editStateUseCase.editState(state, "", project)
            }
        }

        coVerify(exactly = 0) { editProjectStateUseCase.editStateToProject(any(), any(), any()) }
    }


}