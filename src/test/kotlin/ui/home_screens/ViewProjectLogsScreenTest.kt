package ui.home_screens

import creator_helper.projectLogsForAllUsers
import creator_helper.testUserId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.use_cases.log.GetUserProjectLogsUseCase
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.models.User
import org.example.ui.common.screens.ViewProjectLogsUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.example.ui.common.components.Viewer

class ViewProjectLogsScreenTest {

    private val viewer: Viewer = mockk(relaxed = true)
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase = mockk(relaxed = true)
    private val getUserProjectLogsUseCase: GetUserProjectLogsUseCase = mockk(relaxed = true)
    private lateinit var viewProjectLogsUI: ViewProjectLogsUI

    @BeforeEach
    fun setUp() {
        viewProjectLogsUI = ViewProjectLogsUI(viewer, getCurrentLoggedInUserUseCase, getUserProjectLogsUseCase)
    }

    @Test
    fun `should show error when no user is logged in`() {
        // Given
        every { getCurrentLoggedInUserUseCase.getCurrentUser() } returns Result.failure(Exception("No user found"))

        // When
        viewProjectLogsUI.show()

        // Then
        verify(exactly = 1) { viewer.printError("No user logged in") }
    }


    @Test
    fun `should show error when user logs retrieval fails`() {
        // Given
        every { getCurrentLoggedInUserUseCase.getCurrentUser() } returns Result.success(mockk(relaxed = true))
        every { getUserProjectLogsUseCase.getUserProjectLogs(any()) } returns Result.failure(Exception("Unable to fetch logs"))

        // When
        viewProjectLogsUI.show()

        // Then
        verify(exactly = 1) { viewer.printError("Failed to retrieve project logs: Unable to fetch logs") }
    }

    @Test
    fun `should display user logs when logs are retrieved successfully and not empty`() {
        // Given
        val mockUser = mockk<User> {
            every { id } returns testUserId
            every { name } returns "Test User"
        }

        every { getCurrentLoggedInUserUseCase.getCurrentUser() } returns Result.success(mockUser)
        every { getUserProjectLogsUseCase.getUserProjectLogs(testUserId) } returns Result.success(projectLogsForAllUsers)

        // When
        viewProjectLogsUI.show()

        // Then
        verify(exactly = 1) { viewer.printTitle("Project Logs for User: Test User") }

        projectLogsForAllUsers.forEachIndexed { index, log ->
            verify(exactly = 1) {
                viewer.printInfoLine(
                    """
            ${index + 1}.
            - Change made by: ${log.userId}
            - Previous: ${log.previousEntity}
            - Current: ${log.currentEntity}
            - Timestamp: ${log.createdAt}
            """.trimIndent(),
                    true
                )
            }
        }
    }



    @Test
    fun `should notify when user logs are successfully retrieved but empty`() {
        // Given
        val mockUser = mockk<User> {
            every { id } returns testUserId
            every { name } returns "Test User"
        }
        every { getCurrentLoggedInUserUseCase.getCurrentUser() } returns Result.success(mockUser)
        every { getUserProjectLogsUseCase.getUserProjectLogs(testUserId) } returns Result.success(emptyList())

        // When
        viewProjectLogsUI.show()

        // Then
        verify(exactly = 1) { viewer.printInfoLine("No project logs found for the current user.") }
    }

}
