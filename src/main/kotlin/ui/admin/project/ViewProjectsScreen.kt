package org.example.ui.admin.project

import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.models.Project
import org.example.models.User
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.UUID

class ViewProjectsScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val getAllProjectsUseCases: GetAllProjectsUseCases,
    private val singleProjectScreen: SingleProjectScreen,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : UiScreen {
    private var running = true
    override suspend fun show() {
        running = true
        val allProjects = getAllProjectsUseCases.getAllProjects()

        while (running) {
            try {
                if (allProjects.isNotEmpty()) {
                    viewer.printTitle("Project: ")
                    allProjects.forEachIndexed { index, project ->
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

                    chooseProject(allProjects)
                } else {
                    viewer.printInfoLine("No projects found.")
                    running = false
                }
            } catch (e: Exception) {
                viewer.printError("Failed to retrieve projects: ${e.message}")
                running = false
            }
        }
    }

    private suspend fun chooseProject(projects: List<Project>) {
        viewer.printInfoLine("Choose project: ")
        viewer.printOptions(projects.map { it.name } + "Exit")

        enterProject(projects)
    }

    private suspend fun enterProject(projects: List<Project>) {
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

    private suspend fun goToSingleProjectScreen(project: Project) {
        singleProjectScreen.project = project
        singleProjectScreen.show()
    }

    private suspend fun getUserById(id: UUID): User {
        return getUserByIdUseCase.getUser(id)?: throw IllegalStateException("User not found")
    }
}
