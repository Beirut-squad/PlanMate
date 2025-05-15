package ui.view.task

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.model.Task
import domain.model.User
import domain.useCase.authentication.GetCurrentUserUseCase
import domain.useCase.project.GetProjectByIdUseCase
import domain.useCase.task.EditTaskUseCase
import domain.useCase.task.GetProjectTasksUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import ui.view.user.mate.UserProjectsUi
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
                    printer.printInfoLine("No tasks available to edit.")
                    return@tryToExecute
                }
                printer.printTitle("Select a Task to Edit:")
                tasks.forEachIndexed { index, task ->
                    printer.printInfoLine(
                        """
                        Task #${index + 1}
                        - Title: ${task.title}
                        - Description: ${task.description}
                        - State: ${task.taskState.name}
                        """.trimIndent()
                    )
                }
                handleEditTask(tasks, user)
            }
        )
    }

    private suspend fun EditTaskUi.handleEditTask(
        tasks: List<Task>,
        user: User
    ) {
        printer.printLoader("Enter the task number to edit(or any thing to Go Back ):")
        val input = reader.readInput()?.trim()
        val number = input?.toIntOrNull()

        if (number == null || number !in 1..tasks.size) {
            printer.printError("Invalid input. Returning to the projects screen.")
            return
        }

        val selectedTask = tasks[number - 1]
        editSelectedTask(selectedTask, user.id)
    }

    private suspend fun editSelectedTask(task: Task, currentUser: UUID) {
        executor.tryToExecute(
            action = {
                getProjectByIdUseCase.getProjectById(task.projectId)
            },
            onError = {
                handler.printHandledError(it)
            },
            onSuccess = { selectedProject ->
                printer.printTitle("Edit Task - ${task.title}")
                printer.printInfoLine("Current Task Details:")
                printer.printInfoLine(
                    """
            Title: ${task.title}
            Description: ${task.description}
            State: ${task.taskState.name}
            """.trimIndent()
                )

                val newTitle = getValidInput("Enter new Title (Leave empty to keep the current):", task.title)
                val newDescription =
                    getValidInput("Enter new Description (Leave empty to keep the current):", task.description)

                val selectedStateIndex = getValidStateInput(selectedProject, task.taskState.name)
                val selectedState = selectedStateIndex?.let { selectedProject.taskStates[it] } ?: task.taskState

                executor.tryToExecute(
                    action = {
                        editTaskUseCase.editTask(task, newTitle, newDescription, selectedState, currentUser)
                        printer.printInfoLine("Task updated successfully!")
                        UserProjectsUi().show()
                    },
                    onError = {
                        handler.printHandledError(it)
                    }
                )
            }
        )
    }


    private fun getValidInput(message: String, currentValue: String): String? {
            printer.printLoader("$message (Current value: $currentValue)")
            val input = reader.readInput()?.trim()
            return input

    }


    private fun getValidStateInput(selectedProject: Project, currentStateName: String): Int? {
        printer.printOptions("Choose a state for the task (Current: $currentStateName):")
        selectedProject.taskStates.forEachIndexed { index, state ->
            printer.printInfoLine("${index + 1}. ${state.name}")
        }
        printer.printLoader("Enter a number from the list above, or any other number to keep the current state.")

        val input = reader.readInput()?.trim()
        val selectedIndex = input?.toIntOrNull()?.minus(1)

        return if (selectedIndex != null && selectedIndex in selectedProject.taskStates.indices) {
            selectedIndex
        } else {
            printer.printInfoLine("Keeping the current state: $currentStateName")
            null
        }
    }
}