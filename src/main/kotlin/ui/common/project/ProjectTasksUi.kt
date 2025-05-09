package org.example.ui.common.project

import data.csv.model.Task
import domain.use_case.task.GetProjectTasksUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.task.DeleteTaskUI
import org.example.ui.common.task.EditTaskUi
import org.example.ui.mate.UserProjectsUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ProjectTasksUi(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val getTasksForProjectUseCase: GetProjectTasksUseCase by inject()

    override suspend fun show() {
        try {
            val result = getTasksForProjectUseCase.getTasksForProject(projectId)
                printer.printTitle("Tasks for Project:")
                displayTasksInColumns(result)
                printer.printInfoLine("\nPlease choose an option:")
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
        } catch (e: Exception) {
            printer.printError("An error occurred while retrieving tasks: ${e.message}")
        }
    }

    private fun displayTasksInColumns(tasks: List<Task>) {
        val groupedTasks = tasks.groupBy { it.state.name }

        val stateNames = groupedTasks.keys.toList()

        val headers = stateNames.joinToString("") { it.padEnd(30) }
        printer.printInfoLine(headers)

        val separator = "-".repeat(stateNames.size * 30)
        printer.printInfoLine(separator)

        val maxTaskCount = groupedTasks.values.maxOfOrNull { it.size } ?: 0

        for (i in 0 until maxTaskCount) {
            val rowContent = stateNames.joinToString("") { stateName ->
                val task = groupedTasks[stateName]?.getOrNull(i)
                val displayText = task?.name ?: ""
                displayText.padEnd(30)
            }
            printer.printInfoLine(rowContent)
        }
    }
}