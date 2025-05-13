package ui.view.user.admin.project

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.model.State
import domain.use_case.project.GetProjectByIdUseCase
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
    private lateinit var state: State

    fun setProject(project: Project) {
        this.project = project
    }

    fun setState(state: State) {
        this.state = state
    }

    override suspend fun show() {
        while (true) {
            displayHeader()
            displayOptions()

            val input = reader.readInt() ?: -1

            when (input) {
                1 -> {
                    EditProjectStateUi(project, state).show()
                }

                2 -> {
                    DeleteProjectStateUi(project, state).show()
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
        printer.printTitle("State '${state.name}'")
        printer.printInfoLine("What would you like to do?")
    }

    private suspend fun updateProject() {
        executor.tryToExecute(
            action = {
                project = getProjectByIdUseCase.getProjectById(project.id)
                state = project.states.find { it.id == state.id }!! // Warning: Potential crash detected here!
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }
}