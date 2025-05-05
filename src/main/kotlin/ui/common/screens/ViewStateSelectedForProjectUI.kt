package org.example.ui.common.screens

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.models.State
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.*

class ViewStateSelectedForProjectUI(
    private val viewer: Viewer,
    private val projectId: UUID,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId
) : UiScreen {

    override fun show() {
        val states = getProjectStates() ?: return
        if (states.isEmpty()) {
            viewer.printInfoLine("No states available for this project.")
            return
        }
        displayStateOptions(states)
        handleUserSelection(states)
    }

    private fun getProjectStates(): List<State>? {
        viewer.printTitle("State Details")
        var resultStates: List<State>? = null

        getProjectByIdUseCase.getProjectById(projectId).fold(
            onSuccess = { project ->
                resultStates = project.state
            },
            onFailure = {
                viewer.printError("Failed to retrieve project: ${it.message}")
            }
        )
        return resultStates
    }

    private fun displayStateOptions(states: List<State>) {
        states.forEachIndexed { index, state ->
            viewer.printInfoLine("${index + 1}. ${state.name}")
        }
    }

    private fun handleUserSelection(states: List<State>) {
        val choice = viewer.readIntInput("Enter the number of the state to view (0 to cancel): ")

        when {
            choice == null -> viewer.printError("Invalid input.")
            choice == 0 -> viewer.printInfoLine("Cancelled.")
            choice in 1..states.size -> {
                val selectedState = states[choice - 1]
                printStateDetails(selectedState)
            }
            else -> viewer.printError("Invalid choice. Please enter a number between 1 and ${states.size}.")
        }
    }

    private fun printStateDetails(selectedState: State) {
        val result = getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, selectedState.id)
        viewer.printTitle("State Details")
        viewer.printInfoLine("Name: ${selectedState.name}")
        viewer.printInfoLine("Tasks: $result")
    }
}
