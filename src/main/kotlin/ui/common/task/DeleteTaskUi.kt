package org.example.ui.common.task

import org.example.data.model.Task
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

    override suspend fun show() {
        try {
            val tasksResult = getProjectTasksUseCase.getTasksForProject(projectId)
            val user = getCurrentUserUseCase.getCurrentUser()

            if (user == null) {
                printer.printError("No user found")
                return
            }
            if (tasksResult.isEmpty()) {
                printer.printInfoLine("No tasks available to delete.")
                return
            }
            printer.printTitle("Select a Task to Delete:")
            tasksResult.forEachIndexed { index, task ->
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
            if (taskIndex == null || taskIndex !in 1..tasksResult.size) {
                printer.printError("Invalid task number.")
                return
            }

            val selectedTask = tasksResult[taskIndex - 1]
            confirmAndDelete(selectedTask,user.id)
        }catch (e:Exception){
            printer.printError("${e.message}")
        }
    }

    private suspend fun confirmAndDelete(task: Task, currentUser : UUID) {
        printer.printInfoLine("Are you sure you want to delete the task: '${task.title}'? (yes/no)")
        val confirmation = reader.readInput()?.trim()?.lowercase()
        if (confirmation == "yes") {
            try {
                deleteTaskUseCase.deleteTask(task,currentUser)
                printer.printInfoLine("Task deleted successfully.")
                UserProjectsUi().show()
            } catch (e: Exception) {
                printer.printError("Failed to delete task: ${e.message}")
            }
        } else {
            printer.printInfoLine("Deletion cancelled.")
        }
    }
}