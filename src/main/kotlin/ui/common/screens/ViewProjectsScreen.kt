package org.example.ui.common.screens

import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.ui.admin.project.CreateProjectStateUi
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

import java.util.*

class ViewProjectsScreen(
    private val viewer: Viewer,
    private val getAllProjectsUseCases: GetAllProjectsUseCases,
) : UiScreen {
    override fun show() {
        val allProjects = getAllProjectsUseCases.getAllProjects()

        allProjects.fold(
            onSuccess = { projects ->
                if (projects.isNotEmpty()) {
                    viewer.printTitle("Projects: ")
                    projects.forEachIndexed { index, project ->
                        viewer.printInfoLine(
                            """
                        ${index + 1}.
                        - Made by: ${project.creatorUserID}
                        - Name: ${project.name}
                        - Description: ${project.description}
                        - Creation Date: ${project.createdAt}
                        - Update Date: ${project.updatedAt}
                        - State of Project: ${project.state}
                    """.trimIndent()
                        )

                        if (project.state.isEmpty()) {
                            viewer.printError(
                                "Project '${project.name}" +
                                        "' has no state. Redirecting to state creation..."
                            )
                            CreateProjectStateUi(project).show()
                            return
                        }
                    }
                }
                else {
                    viewer.printInfoLine("No projects found.")
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve projects: ${it.message}")
            }
        )
    }
}