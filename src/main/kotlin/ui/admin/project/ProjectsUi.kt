package org.example.ui.admin.project

import domain.model.Project
import domain.model.User
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.project.GetAllProjectsUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import java.util.*

class ProjectsUi(
    private val printer: Printer,
    private val reader: Reader,
    private val getAllProjectsUseCases: GetAllProjectsUseCase,
    private val singleProjectUi: SingleProjectUi,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : UiScreen {
    private var running = true
    override suspend fun show() {
        running = true
        val allProjects = getAllProjectsUseCases.getAllProjects()

        while (running) {
            try {
                if (allProjects.isNotEmpty()) {
                    printer.printTitle("Project: ")
                    allProjects.forEachIndexed { index, project ->
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

                    chooseProject(allProjects)
                } else {
                    printer.printInfoLine("No projects found.")
                    running = false
                }
            } catch (e: Exception) {
                printer.printError("Failed to retrieve projects: ${e.message}")
                running = false
            }
        }
    }

    private suspend fun chooseProject(projects: List<Project>) {
        printer.printInfoLine("Choose project: ")
        printer.printOptions(projects.map { it.title } + "Exit")

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
                enterProject(projects)
            }
        }
    }

    private suspend fun goToSingleProjectScreen(project: Project) {
        singleProjectUi.project = project
        singleProjectUi.show()
    }

    private suspend fun getUserById(id: UUID): User {
        return getUserByIdUseCase.getUser(id)?: throw IllegalStateException("User not found")
    }
}
