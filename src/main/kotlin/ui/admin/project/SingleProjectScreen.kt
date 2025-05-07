package org.example.ui.admin.project

import EditProjectStateUi
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.DeleteProjectUseCase
import org.example.models.Project
import org.example.models.User
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.*

class SingleProjectScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val editProjectScreen: EditProjectScreen
): UiScreen {
    lateinit var project: Project
    val user: User? = getCurrentLoggedInUserUseCase.getCurrentUser().getOrNull()
    private var running = true

    override fun show() {
        running = true
        while (running) {
            viewer.printTitle("Project ${project.name}")
            viewer.printInfoLine("What would you like to do?")

            viewer.printOptions(
                "Edit project",
                "Delete project",
                "View project states",
                "Create new state",
                "Edit state ",
                "Exit"
            )
            takeUserInput()
        }
    }

    private fun takeUserInput() {
        val input = reader.readInt()
        when (input) {
            1 -> {
                editProjectScreen.project = project
                editProjectScreen.show()
            }
            2 -> {
                deleteProjectUseCase.deleteProject(
                    project = project,
                    creatorUserID = user?.id ?: UUID.randomUUID()
                )
                running = false
            }
            3 -> {
                // TODO
            }
            4 -> {
                CreateProjectStateUi(project).show()
            }
            5 -> {
                EditProjectStateUi(project).show()
            }
            6 -> {
                viewer.printGoodbyeMessage("Goodbye!")
                running = false
            }
            else -> {
                viewer.printError("Invalid option")
                takeUserInput()
            }
        }
    }
}