package org.example.ui.admin.project

import domain.exception.handler.ExceptionHandler
import domain.model.Project
import domain.model.State
import domain.use_case.project.GetProjectByIdUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import java.util.*

class ProjectStatesUi(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val singleStateUi: SingleStateUi,
    private val printer: Printer,
    private val reader: Reader,
    private val exceptionHandler: ExceptionHandler,
) : UiScreen {
    private lateinit var projectId: UUID
    private lateinit var project: Project
    private lateinit var states: List<State>

    suspend fun setProject(projectId: UUID) {
        this.projectId = projectId
        this.project = getProject()
        this.states = project.state
    }

    override suspend fun show() {
        while (true) {
            printer.printTitle("Project states")

            if (states.isEmpty()) {
                displayNoStatesAndGoToCreateState()
                break
            } else {
                displayAvailableStates()

                printer.printInfoLine("Choose state number or exit: ")
                val selectedIndex = reader.readInput().toString().toIntOrNull()?.minus(1) ?: -1

                when (selectedIndex) {
                    in states.indices -> {
                        goToSingleStateUi(states[selectedIndex])
                    }

                    states.size -> {
                        break
                    }

                    else -> {
                        printer.printError("Invalid selection")
                    }
                }
            }
            setProject(projectId)
        }
    }

    private suspend fun getProject(): Project {
        return exceptionHandler.tryCatchingAsyncWithResult(
            action = {
                getProjectByIdUseCase.getProjectById(projectId)
            }
        )
    }

    private suspend fun displayNoStatesAndGoToCreateState() {
        printer.printError("You have no states, please create one first")
        CreateProjectStateUi(project).show()
    }

    private fun displayAvailableStates() {
        printer.printCorrectOutput("Available States:")
        printer.printOptions(states.map { it.name } + "Exit")
    }

    private suspend fun goToSingleStateUi(state: State) {
        setProject(projectId)
        singleStateUi.setProject(project)
        singleStateUi.setState(state)
        singleStateUi.show()
    }
}