package org.example.ui.home_screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.ui.Reader
import org.example.ui.UiScreen
import ui.Viewer

class ViewProjectsScreen(
    private val viewer: Viewer,
    private val getAllProjectsUseCases: GetAllProjectsUseCases,
) : UiScreen {
    override fun show() {

        val allProjects = getAllProjectsUseCases.getAllProjects()

        allProjects.fold(
            onSuccess = { projects ->
                if (projects.isNotEmpty()) {
                    viewer.printTitle("Project: ")
                    projects.forEachIndexed { index, project ->
                        viewer.printInfoLine(
                            """
                        ${index + 1}.
                        - Made by: ${project.creatorUserID}
                        - Name: ${project.name}
                        - Description: ${project.description}
                        - Creation Date: ${project.createdAt}
                        - Update Date: ${project.updatedAt}
                    """.trimIndent()
                        )
                    }
                } else {
                    viewer.printInfoLine("No projects found.")
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve projects: ${it.message}")
            }
        )
    }
}
