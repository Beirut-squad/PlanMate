package org.example.ui.admin.project

import domain.model.Project
import domain.model.User
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.DeleteProjectUseCase
import domain.use_case.project.GetProjectByIdUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.task.CreateTaskUi
import java.io.InvalidObjectException

class SingleProjectUi(
    private val printer: Printer,
    private val reader: Reader,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentUserUseCase,
    private val editProjectUi: EditProjectUi,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val viewProjectStates: ProjectStatesUi
) : UiScreen {
    lateinit var project: Project
    lateinit var user: User
    private var running = true

    override suspend fun show() {
        user = getCurrentLoggedInUserUseCase.getCurrentUser() ?: throw InvalidObjectException("User is not logged in")
        running = true
        while (running) {
            printer.printTitle("Project ${project.title}")
            printer.printInfoLine("What would you like to do?")

            printer.printOptions(
                "Edit project",
                "Delete project",
                "View project states",
                "Create new state",
                "Add user to project",
                "Add task to project",
                "Exit"
            )

            takeUserInput()
        }
    }

    private suspend fun takeUserInput() {
        val input = reader.readInt()
        when (input) {
            1 -> {
                editProjectUi.project = project
                editProjectUi.show()
            }

            2 -> {
                try {
                    deleteProjectUseCase.deleteProject(
                        project = project,
                        creatorUserID = user.id
                    )
                    running = false
                } catch (e: Exception) {
                    printer.printError("${e.message}")
                    running = false
                }

            }

            3 -> {
                viewProjectStates.setProject(project.id)
                viewProjectStates.show()
            }

            4 -> {
                CreateProjectStateUi(project).show()
            }

            5 -> {
                AddProjectUserUi(project.id).show()
            }

            6 -> {
                if (project.state.isEmpty()) {
                    printer.printError("Cannot create a task because this project has no states.")
                } else {
                    CreateTaskUi(projectId = project.id).show()
                }
            }

            7 -> {
                printer.printGoodbyeMessage("Goodbye!")
                running = false
            }

            else -> {
                printer.printError("Invalid option")
                takeUserInput()
            }
        }

        if (running) {
            updateProject()
        }

    }

    private suspend fun updateProject() {
        try {
            project = getProjectByIdUseCase.getProjectById(project.id)
        } catch (e: Exception) {
            printer.printError("${e.message}")
        }
    }
}