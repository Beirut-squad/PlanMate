package org.example.ui.admin.project

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.models.Project
import org.example.models.State
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class SingleStateUi(
    private val viewer: Viewer,
    private val reader: Reader,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) : UiScreen {
    private lateinit var project: Project
    private lateinit var state: State

    fun setProject(project: Project) {
        this.project = project
    }

    fun setState(state: State) {
        this.state = state
    }

    override fun show() {
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
                }

                3 -> {
                    // TODO
                }

                4 -> {
                    break
                }

                else -> {
                    viewer.printError("Invalid option")
                }
            }

            updateProject()
        }
    }

    private fun displayOptions() {
        viewer.printOptions(
            "Edit state",
            "Delete state",
            "Add task to state",
            "Exit"
        )
    }

    private fun displayHeader() {
        viewer.printTitle("State '${state.name}'")
        viewer.printInfoLine("What would you like to do?")
    }

    private fun updateProject() {
        try {
            project = getProjectByIdUseCase.getProjectById(project.id)
        } catch (e: Exception) {
            viewer.printError("${e.message}")
        }
    }
}