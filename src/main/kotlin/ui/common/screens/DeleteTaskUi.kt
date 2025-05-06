package org.example.ui.common.screens

import org.example.logic.use_cases.task_managemnt.DeleteTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase
import org.example.models.Task
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
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

    override fun show() {
        val tasksResult = getTasksForProjectUseCase.getTasksForProject(projectId)

        tasksResult.fold(
            onSuccess = { tasks ->
                if (tasks.isEmpty()) {
                    viewer.printInfoLine("No tasks available to delete.")
                    return
                }

                viewer.printTitle("Select a Task to Delete:")
                tasks.forEachIndexed { index, task ->
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
                if (taskIndex == null || taskIndex !in 1..tasks.size) {
                    viewer.printError("Invalid task number.")
                    return
                }

                val selectedTask = tasks[taskIndex - 1]
                confirmAndDelete(selectedTask)

            },
            onFailure = {
                viewer.printError("Failed to retrieve tasks: ${it.message}")
            }
        )
    }

    private fun confirmAndDelete(task: Task) {
        viewer.printInfoLine("Are you sure you want to delete the task: '${task.title}'? (yes/no)")
        val confirmation = reader.readInput()?.trim()?.lowercase()
        if (confirmation == "yes") {
            try {
                deleteTaskUseCase.deleteTask(task,task.id)
                viewer.printInfoLine("Task deleted successfully.")
            } catch (e: Exception) {
                viewer.printError("Failed to delete task: ${e.message}")
            }
        } else {
            viewer.printInfoLine("Deletion cancelled.")
        }
    }
}