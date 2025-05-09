package org.example.ui.common.task

import domain.model.Project
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.GetProjectByIdUseCase
import domain.use_case.task.CreateTaskUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
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

    override suspend fun show() {
        try {
            val selectedProject = getProjectByIdUseCase.getProjectById(projectId)
            val user = getCurrentUserUseCase.getCurrentUser()

            if (user == null) {
                printer.printError("No user found")
                return
            }

            printer.printTitle("Let's create a task")

            val name = getValidInput("Write your task name:")
            val description = getValidInput("Tell me more about description of your task:")

            val selectedStateIndex = getValidStateInput(selectedProject)

            val selectedState = selectedProject.state[selectedStateIndex]
            createTaskUseCase.createTask(name, description, selectedState, selectedProject.id, user.id)
        } catch (e: Exception) {
            printer.printError("Failed to retrieve project: ${e.message}")
        }
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
            selectedProject.state.forEachIndexed { index, state ->
                printer.printInfoLine("${index + 1}. ${state.name}")
            }

            val stateIndexInput = reader.readInput()?.trim()

            if (stateIndexInput.isNullOrBlank()) {
                printer.printError("You must select a state. Please choose a valid number.")
                continue
            }

            selectedStateIndex = stateIndexInput.toIntOrNull()?.minus(1)

            if (selectedStateIndex == null || selectedStateIndex !in selectedProject.state.indices) {
                printer.printError("Invalid state selection. Please choose a valid number between 1 and ${selectedProject.state.size}.")
            }
        } while (selectedStateIndex == null || selectedStateIndex !in selectedProject.state.indices)

        return selectedStateIndex
    }
}