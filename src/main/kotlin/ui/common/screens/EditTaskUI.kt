package org.example.ui.common.screens

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.EditTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase
import org.example.models.Project
import org.example.models.Task
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
import kotlin.getValue

class EditTaskUI(
    private val projectId: UUID
) : UiScreen, KoinComponent {

    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val getTasksForProjectUseCase: GetTasksForProjectUseCase by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    private val editTaskUseCase: EditTaskUseCase by inject()

    override fun show() {
        val tasksResult = getTasksForProjectUseCase.getTasksForProject(projectId)

        tasksResult.fold(
            onSuccess = { tasks ->
                if (tasks.isEmpty()) {
                    viewer.printInfoLine("No tasks available to edit.")
                    return
                }
                viewer.printTitle("Select a Task to Edit:")
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
                viewer.printLoader("Enter the task number to edit(or any thing to exit):")
                val input = reader.readInput()?.trim()
                val number = input?.toIntOrNull()

                if (number == null || number !in 1..tasks.size) {
                    viewer.printError("Invalid input. Returning to the projects screen.")
                    return ViewProjectsForUserUI().show()
                }

                val selectedTask = tasks[number - 1]
                editSelectedTask(selectedTask)

            },
            onFailure = {
                viewer.printError("Failed to retrieve tasks: ${it.message}")
            }
        )
    }

    private fun editSelectedTask(task: Task) {
        getProjectByIdUseCase.getProjectById(task.projectId).fold(
            onSuccess = { selectedProject ->
                viewer.printTitle("Edit Task - ${task.title}")
                viewer.printInfoLine("Current Task Details:")
                viewer.printInfoLine(
                    """
                    Title: ${task.title}
                    Description: ${task.description}
                    State: ${task.state.name}
                    """.trimIndent()
                )

                val newTitle = getValidInput("Enter new Title (Leave empty to keep the current):", task.title)
                val newDescription = getValidInput("Enter new Description (Leave empty to keep the current):", task.description)

                val selectedStateIndex = getValidStateInput(selectedProject, task.state.name)
                val selectedState = selectedStateIndex?.let { selectedProject.state[it] } ?: task.state

                try {
                    editTaskUseCase.editTask(task, newTitle, newDescription, selectedState)
                    viewer.printInfoLine("Task updated successfully!")
                    ViewProjectsForUserUI().show()
                } catch (e: Exception) {
                    viewer.printError("Failed to update task: ${e.message}")
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve project: ${it.message}")
            }
        )
    }

    private fun getValidInput(message: String, currentValue: String): String {
        var input: String?
        do {
            viewer.printLoader("$message (Current value: $currentValue)")
            input = reader.readInput()?.trim()
            if (input.isNullOrBlank()) {
                input = currentValue
            }
        } while (input.isBlank())
        return input
    }

    private fun getValidStateInput(selectedProject: Project, currentStateName: String): Int? {
        viewer.printOptions("Choose a state for the task (Current: $currentStateName):")
        selectedProject.state.forEachIndexed { index, state ->
            viewer.printInfoLine("${index + 1}. ${state.name}")
        }
        viewer.printLoader("Enter a number from the list above, or any other number to keep the current state.")

        val input = reader.readInput()?.trim()
        val selectedIndex = input?.toIntOrNull()?.minus(1)

        return if (selectedIndex != null && selectedIndex in selectedProject.state.indices) {
            selectedIndex
        } else {
            viewer.printInfoLine("Keeping the current state: $currentStateName")
            null
        }
    }
}