package ui.view.user.admin.project

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.model.User
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.project.GetAllProjectsUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import java.util.*

class ProjectsUi(
    private val printer: Printer,
    private val reader: Reader,
    private val getAllProjectsUseCases: GetAllProjectsUseCase,
    private val singleProjectUi: SingleProjectUi,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler
) : UiScreen {
    private var running = true
    override suspend fun show() {
        running = true
        executor.tryToExecute(action = {
            while (running) {
                val projects = getAllProjectsUseCases.getAllProjects()
                showProjectDetails(projects)
            }
        }, onError = {
            handler.printHandledError(it)
        })
    }

    private suspend fun showProjectDetails(projects: List<Project>) {
        executor.tryToExecute(action = {
            if (projects.isNotEmpty()) {
                printer.printTitle("Project: ")
                projects.forEachIndexed { index, project ->
                    printer.printInfoLine(
                        """
                            ${index + 1}.
                            - Made by: ${getUserById(project.creatorUserID).name}
                            - Name: ${project.title}
                            - Description: ${project.description}
                            - Creation Date: ${project.createdAt}
                            - Update Date: ${project.updatedAt}
                        """.trimIndent()
                    )
                }

                chooseProject(projects)
            } else {
                printer.printInfoLine("No projects found.")
                running = false
            }
        }, onError = {
            handler.printHandledError(it)
            running = false
        })
    }

    private suspend fun chooseProject(projects: List<Project>) {
        printer.printInfoLine("Choose project: ")
        printer.printOptions(projects.map { it.title } + "Go Back")
        enterProject(projects)
    }

    private suspend fun enterProject(projects: List<Project>) {
        when (val input = reader.readInt()) {
            in 1..projects.size -> {
                if (input != null) {
                    goToSingleProjectScreen(projects[input - 1])
                } else {
                    printer.printError("Invalid project number")
                    enterProject(projects)
                }
            }

            projects.size + 1 -> {
                running = false
            }

            else -> {
                printer.printError("Invalid project number")
                showProjectDetails(projects)
            }
        }
    }

    private suspend fun goToSingleProjectScreen(project: Project) {
        singleProjectUi.project = project
        singleProjectUi.show()
    }

    private suspend fun getUserById(id: UUID): User {
        return getUserByIdUseCase.getUser(id)
    }
}
