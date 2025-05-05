package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.log.GetUserTaskLogsUseCase
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class ViewTaskLogsScreen(
    private val viewer: Viewer,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val getUserTaskLogsUseCase: GetUserTaskLogsUseCase
): UiScreen {
    override fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()

        val user = currentUserResult.getOrNull()
        if (user == null) {
            viewer.printError("No user logged in")
            return
        }

        val userLogsResult = getUserTaskLogsUseCase.getUserTaskLogs(user.id)

        userLogsResult.fold(
            onSuccess = { logs ->
                if (logs.isNotEmpty()) {
                    viewer.printTitle("Task Logs for User: ${user.name}")
                    logs.forEachIndexed { index, log ->
                        viewer.printInfoLine(
                            """
                        ${index + 1}.
                        - Change made by: ${log.userId}
                        - Previous: ${log.previousEntity}
                        - Current: ${log.currentEntity}
                        - Timestamp: ${log.createdAt}
                    """.trimIndent()
                        )
                    }
                } else {
                    viewer.printInfoLine("No task logs found for the current user.")
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve task logs: ${it.message}")
            }
        )
    }
}