package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase
import org.example.models.Project
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.UUID

class CreateNewTaskUI(
    private val viewer: Viewer,
    private val reader: Reader,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val projectId: UUID,
    private val createTaskUseCase: CreateTaskUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) : UiScreen {

    override fun show() {
        getProjectByIdUseCase.getProjectById(projectId).fold(
            onSuccess = { selectedProject ->
                getCurrentLoggedInUserUseCase.getCurrentUser().getOrNull()?.let { user ->
                    viewer.printTitle("Let's create a task")

                    val name = getValidInput("Write your task name:")
                    val description = getValidInput("Tell me more about description of your task:")

                    val selectedStateIndex = getValidStateInput(selectedProject)

                    if (selectedStateIndex != -1) {
                        val selectedState = selectedProject.state[selectedStateIndex]
                        createTaskUseCase.createTask(name, description, selectedState, selectedProject.id, user.id)
                    }
                } ?: viewer.printError("No user found")
            },
            onFailure = { viewer.printError("Failed to retrieve project: ${it.message}") }
        )
    }


    private fun getValidInput(massage: String): String {
        var input: String?
        do {
            viewer.printInfoLine(massage)
            input = reader.readInput()?.trim()
            if (input.isNullOrBlank()) {
                viewer.printError("Input cannot be empty or null. Please try again.")
            }
        } while (input.isNullOrBlank())
        return input
    }

    private fun getValidStateInput(selectedProject: Project): Int {
        var isValidState = false
        var selectedStateIndex: Int? = null
        do {
            viewer.printOptions("Choose a state for the task:")
            selectedProject.state.forEachIndexed { index, state ->
                viewer.printInfoLine("${index + 1}. ${state.name}")
            }

            val stateIndexInput = reader.readInput()?.trim()

            if (stateIndexInput.isNullOrBlank()) {
                viewer.printError("You must select a state. Please choose a valid number.")
                continue
            }

            selectedStateIndex = stateIndexInput.toIntOrNull()?.minus(1)

            if (selectedStateIndex == null || selectedStateIndex !in selectedProject.state.indices) {
                viewer.printError("Invalid state selection. Please choose a valid number between 1 and ${selectedProject.state.size}.")
            } else {
                isValidState = true
            }
        } while (!isValidState)

        return selectedStateIndex ?: -1
    }


}