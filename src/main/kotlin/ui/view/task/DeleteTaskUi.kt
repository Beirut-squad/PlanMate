package ui.view.task

import domain.exception.handler.SafeExecutor
import domain.model.Task
import domain.model.User
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.task.DeleteTaskUseCase
import domain.use_case.task.GetProjectTasksUseCase
import domain.exception.handler.ExceptionHandler
import ui.components.Printer
import ui.components.Reader
import ui.components.UiScreen
import ui.view.user.mate.UserProjectsUi
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
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    override suspend fun show() {
        executor.tryToExecute(
            action = {
                getProjectTasksUseCase.getProjectTasks(projectId)
            },
            onError = {
                handler.printHandledError(it)
            },
            onSuccess = { tasks ->
                val user = getCurrentUserUseCase.getCurrentUser()

                if (tasks.isEmpty()) {
                    printer.printInfoLine("No tasks available to delete.")
                    return@tryToExecute
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
                handleTaskDeletion(tasks, user)
            }
        )
    }

    private suspend fun DeleteTaskUI.handleTaskDeletion(
        tasks: List<Task>,
        user: User
    ) {
        printer.printLoader("Enter the task number to delete:")
        val taskIndex = reader.readInput()?.toIntOrNull()
        if (taskIndex == null || taskIndex !in 1..tasks.size) {
            printer.printError("Invalid task number.")
            return
        }

        val selectedTask = tasks[taskIndex - 1]
        confirmAndDelete(selectedTask, user.id)
        UserProjectsUi().show()
    }

    private suspend fun confirmAndDelete(task: Task, currentUser: UUID) {
        printer.printInfoLine("Are you sure you want to delete the task: '${task.title}'? (yes/no)")
        val confirmation = reader.readInput()?.trim()?.lowercase()
        if (confirmation == "yes") {
            executor.tryToExecute(
                action = {
                    deleteTaskUseCase.deleteTask(task, currentUser)
                    printer.printInfoLine("Task deleted successfully.")
                    UserProjectsUi().show()
                },
                onError = {
                    handler.printHandledError(it)
                }
            )
        } else {
            printer.printInfoLine("Deletion cancelled.")
        }
    }
}