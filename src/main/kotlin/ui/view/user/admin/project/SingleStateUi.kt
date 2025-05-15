package ui.view.user.admin.project

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.model.TaskState
import domain.useCase.project.GetProjectByIdUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen

class SingleStateUi(
    private val printer: Printer,
    private val reader: Reader,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
) : UiScreen {
    private lateinit var project: Project
    private lateinit var taskState: TaskState

    fun setProject(project: Project) {
        this.project = project
    }

    fun setState(taskState: TaskState) {
        this.taskState = taskState
    }

    override suspend fun show() {
        while (true) {
            displayHeader()
            displayOptions()

            val input = reader.readInt() ?: -1

            when (input) {
                1 -> {
                    EditProjectStateUi(project, taskState).show()
                }

                2 -> {
                    DeleteProjectStateUi(project, taskState).show()
                    break
                }

                3 -> {
                    break
                }

                else -> {
                    printer.printError("Invalid option")
                }
            }

            updateProject()
        }
    }

    private fun displayOptions() {
        printer.printOptions(
            "Edit state",
            "Delete state",
            "Go Back"
        )
    }

    private fun displayHeader() {
        printer.printTitle("State '${taskState.name}'")
        printer.printInfoLine("What would you like to do?")
    }

    private suspend fun updateProject() {
        executor.tryToExecute(
            action = {
                project = getProjectByIdUseCase.getProjectById(project.id)
                taskState = project.taskStates.find { it.id == taskState.id }!! // Warning: Potential crash detected here!
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }
}