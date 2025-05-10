package org.example.ui.common.task

import domain.exception.handler.ExceptionHandler
import domain.model.Task
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.task.DeleteTaskUseCase
import domain.use_case.task.GetProjectTasksUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.mate.UserProjectsUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class DeleteTaskUI(
    private val projectId: UUID
) : UiScreen, KoinComponent {

    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val getProjectTasksUseCase: GetProjectTasksUseCase by inject()
    private val deleteTaskUseCase: DeleteTaskUseCase by inject()
    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val exceptionHandler: ExceptionHandler by inject()

    override suspend fun show() {
        exceptionHandler.runSafely {
            getProjectTasksUseCase.getTasksForProject(projectId)
        }.onSuccess { tasks ->
            val user = getCurrentUserUseCase.getCurrentUser()

            if (tasks.isEmpty()) {
                printer.printInfoLine("No tasks available to delete.")
                return
            }
            printer.printTitle("Select a Task to Delete:")
            tasks.forEachIndexed { index, task ->
                printer.printInfoLine(
                    """
                        Task #${index + 1}
                        - Title: ${task.title}
                        - Description: ${task.description}
                        - State: ${task.state.name}
                        """.trimIndent()
                )
            }
            printer.printLoader("Enter the task number to delete:")
            val taskIndex = reader.readInput()?.toIntOrNull()
            if (taskIndex == null || taskIndex !in 1..tasks.size) {
                printer.printError("Invalid task number.")
                return
            }

            val selectedTask = tasks[taskIndex - 1]
            confirmAndDelete(selectedTask, user.id)
        }
    }

    private suspend fun confirmAndDelete(task: Task, currentUser: UUID) {
        printer.printInfoLine("Are you sure you want to delete the task: '${task.title}'? (yes/no)")
        val confirmation = reader.readInput()?.trim()?.lowercase()
        if (confirmation == "yes") {
            exceptionHandler.runSafely {
                deleteTaskUseCase.deleteTask(task, currentUser)
            }.onSuccess {
                printer.printInfoLine("Task deleted successfully.")
                UserProjectsUi().show()
            }
        } else {
            printer.printInfoLine("Deletion cancelled.")
        }
    }
}