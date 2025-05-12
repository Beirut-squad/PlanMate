package org.example.ui.admin.project

import domain.exception.handler.ExceptionHandler
import domain.model.Project
import domain.model.State
import domain.use_case.project.GetProjectByIdUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen

class SingleStateUi(
    private val printer: Printer,
    private val reader: Reader,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val exceptionHandler: ExceptionHandler,
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
            "Exit"
        )
    }

    private fun displayHeader() {
        printer.printTitle("State '${state.name}'")
        printer.printInfoLine("What would you like to do?")
    }

    private suspend fun updateProject() {
        exceptionHandler.tryCatchingAsync(
            action = {
                project = getProjectByIdUseCase.getProjectById(project.id)
                state = project.state.find { it.id == state.id }!! // Warning: Potential crash detected here!
            }
        )
    }
}