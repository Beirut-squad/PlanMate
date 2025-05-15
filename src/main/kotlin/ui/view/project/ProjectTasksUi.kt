package ui.view.project

import ui.common.exception.handler.SafeExecutor
import domain.model.Task
import domain.useCase.task.GetProjectTasksUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import ui.view.task.DeleteTaskUI
import ui.view.task.EditTaskUi
import ui.view.user.mate.UserProjectsUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ProjectTasksUi(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val getTasksForProjectUseCase: GetProjectTasksUseCase by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    override suspend fun show() {
        executor.tryToExecute(
            action = {
                getTasksForProjectUseCase.getProjectTasks(projectId)
            },
            onError = {
                handler.printHandledError(it)
            },
            onSuccess = {
                if (it.isNotEmpty()) {
                    printer.printTitle("Tasks for Project:")
                }
                displayTasksInColumns(it)
                printer.printInfoLine("Please choose an option:")
                printer.printOptions("Edit a task", "Delete a task", "Enter Any Thing To Go Back")
                val choice = reader.readInput()?.toIntOrNull()
                when (choice) {
                    1 -> {
                        EditTaskUi(projectId).show()
                    }

                    2 -> {
                        DeleteTaskUI(projectId).show()
                    }

                    else -> {
                        printer.printGoodbyeMessage("Goodbye")
                        UserProjectsUi().show()
                    }
                }
            }
        )
    }

    private fun displayTasksInColumns(tasks: List<Task>) {
        val groupedTasks = tasks.groupBy { it.taskState.name }

        val stateNames = groupedTasks.keys.toList()

        val headers = stateNames.joinToString("") { it.padEnd(30) }
        printer.printInfoLine(headers)

        val separator = "-".repeat(stateNames.size * 30)
        printer.printInfoLine(separator)

        val maxTaskCount = groupedTasks.values.maxOfOrNull { it.size } ?: 0

        for (i in 0 until maxTaskCount) {
            val rowContent = stateNames.joinToString("") { stateName ->
                val task = groupedTasks[stateName]?.getOrNull(i)
                val displayText = task?.title ?: ""
                displayText.padEnd(30)
            }
            printer.printInfoLine(rowContent)
        }
    }
}