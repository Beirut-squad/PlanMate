package org.example.ui.common.screens

import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class ViewProjectsUI(
    private val viewer: Viewer,
    private val getAllProjectsUseCases: GetAllProjectsUseCases,
) : UiScreen {
    override suspend fun show() {
        try {
            val projects = getAllProjectsUseCases.getAllProjects()
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
                viewer.printError("No projects found.")
            }
        } catch (e: Exception) {
            viewer.printError("Failed to retrieve projects: ${e.message}")
        }
    }
}
