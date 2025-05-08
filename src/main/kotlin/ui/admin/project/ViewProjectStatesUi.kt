package org.example.ui.admin.project

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.models.Project
import org.example.models.State
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.*

class ViewProjectStatesUi(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val singleStateUi: SingleStateUi,
    private val viewer: Viewer,
    private val reader: Reader
) : UiScreen {
    private lateinit var projectId: UUID
    private lateinit var project: Project
    private lateinit var states: List<State>

    fun setProject(projectId: UUID) {
        this.projectId = projectId
        this.project = getProject()
        this.states = project.state
    }

    override suspend fun show() {
        while (true) {
            viewer.printTitle("Project states")

            if (states.isEmpty()) {
                displayNoStatesAndGoToCreateState()
                break
            } else {
                displayAvailableStates()

                viewer.printTitle("Choose state number or exit: ")
                val selectedIndex = reader.readInput().toString().toIntOrNull()?.minus(1) ?: -1

                when (selectedIndex) {
                    in states.indices -> {
                        goToSingleStateUi(states[selectedIndex])
                    }
                    states.size -> {
                        break
                    }
                    else -> {
                        viewer.printError("Invalid selection")
                    }
                }
            }
            setProject(projectId)
        }
    }

    private fun getProject(): Project {
        return getProjectByIdUseCase.getProjectById(projectId)
    }

    private suspend fun displayNoStatesAndGoToCreateState() {
        viewer.printTitle("You have no states, please create one first")
        CreateProjectStateUi(project).show()
    }

    private fun displayAvailableStates() {
        viewer.printTitle("Available States:")
        viewer.printOptions(states.map { it.name } + "Exit")
    }

    private suspend fun goToSingleStateUi(state: State) {
        setProject(projectId)
        singleStateUi.setProject(project)
        singleStateUi.setState(state)
        singleStateUi.show()
    }
}