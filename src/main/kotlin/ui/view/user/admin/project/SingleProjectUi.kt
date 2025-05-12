package ui.view.user.admin.project

import domain.exception.handler.SafeExecutor
import domain.model.Project
import domain.model.User
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.DeleteProjectUseCase
import domain.use_case.project.GetProjectByIdUseCase
import domain.exception.handler.ExceptionHandler
import ui.components.Printer
import ui.components.Reader
import ui.components.UiScreen
import ui.view.task.CreateTaskUi

class SingleProjectUi(
    private val printer: Printer,
    private val reader: Reader,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentUserUseCase,
    private val editProjectUi: EditProjectUi,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val viewProjectStates: ProjectStatesUi,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler
) : UiScreen {
    lateinit var project: Project
    lateinit var user: User
    private var running = true

    override suspend fun show() {
        user = getCurrentLoggedInUserUseCase.getCurrentUser()
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
                "Go Back"
            )
            executor.tryToExecute(
                action = {
                    takeUserInput()
                },
                onError = {
                    handler.printHandledError(it)
                }
            )
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
                deleteProjectUseCase.deleteProject(
                    project = project,
                    creatorUserID = user.id
                )
                running = false
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
                if (project.states.isEmpty()) {
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

            updateProject()


    }

    private suspend fun updateProject() {
        project = getProjectByIdUseCase.getProjectById(project.id)
    }
}