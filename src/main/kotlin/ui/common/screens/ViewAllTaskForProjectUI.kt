package org.example.ui.common.screens

import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
import kotlin.getValue

class ViewAllTaskForProjectUI(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val getTasksForProjectUseCase: GetTasksForProjectUseCase by inject()
    override fun show() {
        val result = getTasksForProjectUseCase.getTasksForProject(projectId)
        result.fold(
            onSuccess = { tasks ->
                if (tasks.isEmpty()) {
                    viewer.printInfoLine("No tasks found for this project.")
                    ViewProjectsForUserUI().show()
                } else {
                    viewer.printTitle("Tasks for Project:")
                    tasks.forEachIndexed { index, task ->
                        viewer.printInfoLine(
                            """
                            Task #${index + 1}
                            - Name: ${task.title}
                            - Description: ${task.description}
                            - State: ${task.state.name}
                            """.trimIndent()
                        )
                    }
                    viewer.printInfoLine("\nPlease choose an option:")
                    viewer.printOptions("Edit a task", "Delete a task", "Enter Any Thing To Go Back")
                    val choice = reader.readInput()?.toIntOrNull()
                    when (choice) {
                        1 -> {

                            EditTaskUI(projectId).show()
                        }

                        2 -> {
                            DeleteTaskUI(projectId).show()
                        }

                        else -> {
                            viewer.printGoodbyeMessage("Goodbye")
                            ViewProjectsForUserUI().show()

                        }
                    }
                }
            },
            onFailure = {
                viewer.printError("An error occurred while retrieving tasks: ${it.message}")
            }
        )

    }
}