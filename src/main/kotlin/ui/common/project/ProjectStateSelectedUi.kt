package org.example.ui.common.project

import domain.exception.handler.SafeExecutor
import domain.model.State
import domain.use_case.project.GetProjectByIdUseCase
import domain.use_case.task.GetTaskByStateIdAndProjectId
import org.example.core.domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ProjectStateSelectedUi(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()
    private var running = true

    override suspend fun show() {
        running = true
        val states = getProjectStates()
        while (running) {
            if (states.isEmpty()) {
                printer.printInfoLine("No states available for this project.")
                running = false
            }
            displayStateOptions(states)
            handleUserSelection(states)
        }
    }

    private suspend fun getProjectStates(): List<State> {
        printer.printTitle("State Details")
        return getProjectByIdUseCase.getProjectById(projectId).states
    }

    private fun displayStateOptions(states: List<State>) {
        states.forEachIndexed { index, state ->
            printer.printInfoLine("${index + 1}. ${state.name}")
        }
    }

    private suspend fun handleUserSelection(states: List<State>) {
        val choice = printer.readIntInput(
            "Enter the number of the state to view (Enter Any Thing To Go Back): "
        )
        when {
            choice != null && choice in 1..states.size -> {
                val selectedState = states[choice - 1]
                printStateDetails(selectedState)
                running = false
            }

            else -> {
                printer.printGoodbyeMessage("Goodbye")
                running = false
            }
        }
    }

    private suspend fun printStateDetails(selectedState: State) {
        printer.printTitle("State Details")
        printer.printInfoLine("Name: ${selectedState.name}")

        executor.tryToExecute(
            action = {
                val tasks = getTaskByStateIdAndProjectId
                    .getTaskByStateIdAndProjectId(projectId, selectedState.id)

                if (tasks.isNotEmpty()) {
                    printer.printInfoLine("Tasks:")
                    tasks.forEach { task ->
                        printer.printInfoLine(
                            " - Name: ${task.title}, Description: ${task.description}"
                        )
                    }
                } else {
                    printer.printInfoLine("No tasks available for this state.")
                    running = false
                }
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }
}
