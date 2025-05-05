package org.example.ui.common.screens

import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.models.State
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.*

class ViewStateSelectedForProjectUI(
    private val viewer: Viewer,
    private val states: List<State>,
    private val projectId: UUID,
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId
):UiScreen {
    override fun show() {

        viewer.printTitle("State Details")

        states.forEachIndexed { index, state ->
            viewer.printInfoLine("${index + 1}. ${state.name}")
        }
        val choice = viewer.readIntInput("Enter the number of the state to view (0 to cancel): ")

        if (choice in 1..states.size) {
            val selectedState = states.getOrNull(choice?.minus(1) ?: -1)
            if (selectedState != null) {
                PrintState(selectedState)
            } else {
                viewer.printError("Invalid state selected.")
            }
        } else if (choice == 0) {
            viewer.printInfoLine("Cancelled.")
        } else {
            viewer.printError("Invalid choice. Please enter a number between 1 and ${states.size}.")
        }
    }
    private fun PrintState(selectedState : State){
        val result = getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, selectedState.id)
        viewer.printTitle("State Details")
        viewer.printInfoLine("Name: ${selectedState.name}")
        viewer.printInfoLine("task: $result")
    }
}