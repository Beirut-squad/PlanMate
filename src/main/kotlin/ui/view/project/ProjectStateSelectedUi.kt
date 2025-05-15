package ui.view.project

import ui.common.exception.handler.SafeExecutor
import domain.model.TaskState
import domain.useCase.project.GetProjectByIdUseCase
import domain.useCase.task.GetTaskByStateIdAndProjectId
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.UiScreen
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

    private suspend fun getProjectStates(): List<TaskState> {
        printer.printTitle("State Details")
        return getProjectByIdUseCase.getProjectById(projectId).taskStates
    }

    private fun displayStateOptions(taskStates: List<TaskState>) {
        taskStates.forEachIndexed { index, state ->
            printer.printInfoLine("${index + 1}. ${state.name}")
        }
        printer.printOption("${taskStates.size + 1}. Go Back")
    }

    private suspend fun handleUserSelection(taskStates: List<TaskState>) {
        val choice = printer.readIntInput(
            "Enter the number of the state to view: "
        )
        when {
            choice != null && choice in 1..taskStates.size -> {
                val selectedState = taskStates[choice - 1]
                printStateDetails(selectedState)
                running = false
            }

            choice == taskStates.size + 1 -> {
                running = false
            }

            else -> {
                printer.printError("Invalid option")
            }
        }
    }

    private suspend fun printStateDetails(selectedTaskState: TaskState) {
        printer.printTitle("State Details")
        printer.printInfoLine("Name: ${selectedTaskState.name}")

        executor.tryToExecute(
            action = {
                val tasks = getTaskByStateIdAndProjectId
                    .getTaskByStateIdAndProjectId(projectId, selectedTaskState.id)

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
