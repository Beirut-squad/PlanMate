package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.models.Project
import org.example.models.User
import org.example.ui.admin.project.SingleProjectScreen
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class ViewProjectsScreen(

) : UiScreen , KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val getAllProjectsUseCases: GetAllProjectsUseCases by inject()
    private val getUserByIdUseCase: GetUserByIdUseCase by inject()
    private var running = true
    override fun show() {
        running = true
        val allProjects = getAllProjectsUseCases.getAllProjects()

        while (running) {
            allProjects.fold(
                onSuccess = { projects ->
                    if (projects.isNotEmpty()) {
                        viewer.printTitle("Project: ")
                        projects.forEachIndexed { index, project ->
                            viewer.printInfoLine(
                                """
                        ${index + 1}.
                        - Made by: ${getUserById(project.creatorUserID).name}
                        - Name: ${project.name}
                        - Description: ${project.description}
                        - Creation Date: ${project.createdAt}
                        - Update Date: ${project.updatedAt}
                    """.trimIndent()
                            )
                        }

                        chooseProject(projects)
                    } else {
                        viewer.printInfoLine("No projects found.")
                        running = false
                    }
                },
                onFailure = {
                    viewer.printError("Failed to retrieve projects: ${it.message}")
                }
            )
        }
    }

    private fun chooseProject(projects: List<Project>) {
        viewer.printInfoLine("Choose project: ")
        viewer.printOptions(projects.map { it.name } + "Exit")

        enterProject(projects)
    }

    private fun enterProject(projects: List<Project>) {
        when (val input = reader.readInt()) {
            in 1..projects.size -> {
                if (input != null) {
                    goToSingleProjectScreen(projects[input - 1])
                } else {
                    viewer.printError("Invalid project number")
                    enterProject(projects)
                }
            }
            projects.size + 1 -> {
                running = false
            }
            else -> {
                viewer.printError("Invalid project number")
                enterProject(projects)
            }
        }
    }

    private fun goToSingleProjectScreen(project: Project) {
        ViewProjectForMateUI(project.id).show()

    }

    private fun getUserById(id: UUID): User {
        return getUserByIdUseCase.getUser(id).getOrThrow() ?: throw IllegalStateException("User not found")
    }
}