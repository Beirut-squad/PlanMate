package ui.view.task

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.GetProjectByIdUseCase
import domain.use_case.task.CreateTaskUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class CreateTaskUi(
    private val projectId: UUID,
) : UiScreen, KoinComponent {

    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val createTaskUseCase: CreateTaskUseCase by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    override suspend fun show() {
        executor.tryToExecute(
            action = {
                val selectedProject = getProjectByIdUseCase.getProjectById(projectId)
                val user = getCurrentUserUseCase.getCurrentUser()

                printer.printTitle("Let's create a task")

                val name = getValidInput("Write your task name:")
                val description = getValidInput("Tell me more about description of your task:")

                val selectedStateIndex = getValidStateInput(selectedProject)

                val selectedState = selectedProject.states[selectedStateIndex]
                createTaskUseCase.createTask(name, description, selectedState, selectedProject.id, user.id)
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }


    private fun getValidInput(massage: String): String {
        var input: String?
        do {
            printer.printInfoLine(massage)
            input = reader.readInput()?.trim()
            if (input.isNullOrBlank()) {
                printer.printError("Input cannot be empty or null. Please try again.")
            }
        } while (input.isNullOrBlank())
        return input
    }

    private fun getValidStateInput(selectedProject: Project): Int {
        var selectedStateIndex: Int? = null
        do {
            printer.printOptions("Choose a state for the task:")
            selectedProject.states.forEachIndexed { index, state ->
                printer.printInfoLine("${index + 1}. ${state.name}")
            }

            val stateIndexInput = reader.readInput()?.trim()

            if (stateIndexInput.isNullOrBlank()) {
                printer.printError("You must select a state. Please choose a valid number.")
                continue
            }

            selectedStateIndex = stateIndexInput.toIntOrNull()?.minus(1)

            if (selectedStateIndex == null || selectedStateIndex !in selectedProject.states.indices) {
                printer.printError("Invalid state selection. Please choose a valid number between 1 and ${selectedProject.states.size}.")
            }
        } while (selectedStateIndex == null || selectedStateIndex !in selectedProject.states.indices)

        return selectedStateIndex
    }
}