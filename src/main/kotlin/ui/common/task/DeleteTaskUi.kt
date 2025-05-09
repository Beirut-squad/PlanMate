package org.example.ui.common.task

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.task_managemnt.DeleteTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase
import org.example.models.Task
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.example.ui.mate.ViewProjectsForUserUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
import kotlin.getValue

class DeleteTaskUI(
    private val projectId: UUID
) : UiScreen, KoinComponent {

    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val getTasksForProjectUseCase: GetTasksForProjectUseCase by inject()
    private val deleteTaskUseCase: DeleteTaskUseCase by inject()
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase by inject()

    override suspend fun show() {
        try {
            val tasksResult = getTasksForProjectUseCase.getTasksForProject(projectId)
            val user = getCurrentLoggedInUserUseCase.getCurrentUser()

            if (user == null) {
                viewer.printError("No user found")
                return
            }
            if (tasksResult.isEmpty()) {
                viewer.printInfoLine("No tasks available to delete.")
                return
            }
            viewer.printTitle("Select a Task to Delete:")
            tasksResult.forEachIndexed { index, task ->
                viewer.printInfoLine(
                    """
                        Task #${index + 1}
                        - Title: ${task.title}
                        - Description: ${task.description}
                        - State: ${task.state.name}
                        """.trimIndent()
                )
            }
            viewer.printLoader("Enter the task number to delete:")
            val taskIndex = reader.readInput()?.toIntOrNull()
            if (taskIndex == null || taskIndex !in 1..tasksResult.size) {
                viewer.printError("Invalid task number.")
                return
            }

            val selectedTask = tasksResult[taskIndex - 1]
            confirmAndDelete(selectedTask,user.id)
        }catch (e:Exception){
            viewer.printError("${e.message}")
        }
    }

    private suspend fun confirmAndDelete(task: Task,currentUser : UUID) {
        viewer.printInfoLine("Are you sure you want to delete the task: '${task.title}'? (yes/no)")
        val confirmation = reader.readInput()?.trim()?.lowercase()
        if (confirmation == "yes") {
            try {
                deleteTaskUseCase.deleteTask(task,currentUser)
                viewer.printInfoLine("Task deleted successfully.")
                ViewProjectsForUserUi().show()
            } catch (e: Exception) {
                viewer.printError("Failed to delete task: ${e.message}")
            }
        } else {
            viewer.printInfoLine("Deletion cancelled.")
        }
    }
}