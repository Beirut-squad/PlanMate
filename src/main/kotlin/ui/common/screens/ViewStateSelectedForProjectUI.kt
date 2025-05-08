package org.example.ui.common.screens

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.models.State
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.example.ui.mate.home_screen.ViewProjectsForUserUI
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ViewStateSelectedForProjectUI(
    private val projectId: UUID,
) : UiScreen,KoinComponent {
    private val viewer: Viewer by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId by inject()
    private var running = true
    override fun show() {
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
        val choice = viewer.readIntInput("Enter the number of the state to view (Enter Any Thing To Go Back): ")
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

    private fun printStateDetails(selectedState: State) {
        val result = getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, selectedState.id)
        viewer.printTitle("State Details")
        viewer.printInfoLine("Name: ${selectedState.name}")

        if (result.isSuccess) {
            val tasks = result.getOrNull() ?: emptyList()

            if (tasks.isNotEmpty()) {
                viewer.printInfoLine("Tasks:")
                tasks.forEach { task ->
                    viewer.printInfoLine(" - Name: ${task.title}, Description: ${task.description}")
                }
            } else {
                viewer.printInfoLine("No tasks available for this state.")
                running = false
            }
        } else {
            viewer.printError("Failed to retrieve tasks: ${result.exceptionOrNull()?.message}")
        }
    }
}
