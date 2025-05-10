package org.example.ui.common.task

import domain.exception.handler.ExceptionHandler
import domain.model.Project
import domain.model.Task
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.GetProjectByIdUseCase
import domain.use_case.task.EditTaskUseCase
import domain.use_case.task.GetProjectTasksUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.mate.UserProjectsUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class EditTaskUi(
    private val projectId: UUID
) : UiScreen, KoinComponent {

    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val getProjectTasksUseCase: GetProjectTasksUseCase by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    private val editTaskUseCase: EditTaskUseCase by inject()
    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val exceptionHandler: ExceptionHandler by inject()

    override suspend fun show() {
        exceptionHandler.runSafely {
            getProjectTasksUseCase.getTasksForProject(projectId)
        }.onSuccess { tasks ->
            val user = getCurrentUserUseCase.getCurrentUser()

            if (tasks.isEmpty()) {
                printer.printInfoLine("No tasks available to edit.")
                return
            }
            printer.printTitle("Select a Task to Edit:")
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
            printer.printLoader("Enter the task number to edit(or any thing to exit):")
            val input = reader.readInput()?.trim()
            val number = input?.toIntOrNull()

            if (number == null || number !in 1..tasks.size) {
                printer.printError("Invalid input. Returning to the projects screen.")
                return UserProjectsUi().show()
            }

            val selectedTask = tasks[number - 1]
            editSelectedTask(selectedTask, user.id)
        }
    }

    private suspend fun editSelectedTask(task: Task, currentUser: UUID) {
        exceptionHandler.runSafely {
            getProjectByIdUseCase.getProjectById(task.projectId)
        }.onSuccess { selectedProject ->
            printer.printTitle("Edit Task - ${task.title}")
            printer.printInfoLine("Current Task Details:")
            printer.printInfoLine(
                """
            Title: ${task.title}
            Description: ${task.description}
            State: ${task.state.name}
            """.trimIndent()
            )

            val newTitle = getValidInput("Enter new Title (Leave empty to keep the current):", task.title)
            val newDescription =
                getValidInput("Enter new Description (Leave empty to keep the current):", task.description)

            val selectedStateIndex = getValidStateInput(selectedProject, task.state.name)
            val selectedState = selectedStateIndex?.let { selectedProject.state[it] } ?: task.state

            exceptionHandler.runSafely {
                editTaskUseCase.editTask(task, newTitle, newDescription, selectedState, currentUser)
            }.onSuccess {
                printer.printInfoLine("Task updated successfully!")
                UserProjectsUi().show()
            }
        }
    }


    private fun getValidInput(message: String, currentValue: String): String {
        while (true) {
            printer.printLoader("$message (Current value: $currentValue)")
            val input = reader.readInput()?.trim()

            if (!input.isNullOrBlank()) {
                return input
            }

            if (currentValue.isNotBlank()) {
                return currentValue
            }

            printer.printError("Input cannot be blank.")
        }
    }


    private fun getValidStateInput(selectedProject: Project, currentStateName: String): Int? {
        printer.printOptions("Choose a state for the task (Current: $currentStateName):")
        selectedProject.state.forEachIndexed { index, state ->
            printer.printInfoLine("${index + 1}. ${state.name}")
        }
        printer.printLoader("Enter a number from the list above, or any other number to keep the current state.")

        val input = reader.readInput()?.trim()
        val selectedIndex = input?.toIntOrNull()?.minus(1)

        return if (selectedIndex != null && selectedIndex in selectedProject.state.indices) {
            selectedIndex
        } else {
            printer.printInfoLine("Keeping the current state: $currentStateName")
            null
        }
    }
}