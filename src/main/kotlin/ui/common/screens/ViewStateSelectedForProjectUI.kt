package org.example.ui.common.screens

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.models.State
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ViewStateSelectedForProjectUI(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId by inject()
    private var running = true
    override suspend fun show() {
        running = true
        while (running) {
            val states = getProjectStates() ?: return
            if (states.isEmpty()) {
                viewer.printInfoLine("No states available for this project.")
                running = false
            }
            displayStateOptions(states)
            handleUserSelection(states)
        }
    }

    private suspend fun getProjectStates(): List<State>? {
        viewer.printTitle("State Details")

        return try {
            val project = getProjectByIdUseCase.getProjectById(projectId)
            project.state
        } catch (e: Exception) {
            viewer.printError("${e.message}")
            null
        }
    }

    private fun displayStateOptions(states: List<State>) {
        states.forEachIndexed { index, state ->
            viewer.printInfoLine("${index + 1}. ${state.name}")
        }
    }

    private suspend fun handleUserSelection(states: List<State>) {
        val choice = viewer.readIntInput(
            "Enter the number of the state to view (Enter Any Thing To Go Back): ")
        when {
            choice != null && choice in 1..states.size -> {
                val selectedState = states[choice - 1]
                printStateDetails(selectedState)
                running = false
            }

            else -> {
                viewer.printGoodbyeMessage("Goodbye")
                running = false
            }
        }
    }

    private suspend fun printStateDetails(selectedState: State) {
        viewer.printTitle("State Details")
        viewer.printInfoLine("Name: ${selectedState.name}")

        try {
            val tasks = getTaskByStateIdAndProjectId
                                .getTaskByStateIdAndProjectId(projectId, selectedState.id)

            if (tasks.isNotEmpty()) {
                viewer.printInfoLine("Tasks:")
                tasks.forEach { task ->
                    viewer.printInfoLine(
                        " - Name: ${task.title}, Description: ${task.description}")
                }
            } else {
                viewer.printInfoLine("No tasks available for this state.")
                running = false
            }
        } catch (e:Exception){
            viewer.printError("${e.message}")
        }
    }
}
