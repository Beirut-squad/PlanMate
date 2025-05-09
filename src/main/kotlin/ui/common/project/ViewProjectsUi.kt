package org.example.ui.common.project

import domain.use_case.project.GetAllProjectsUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class ViewProjectsUi(
    private val printer: Printer,
    private val getAllProjectsUseCases: GetAllProjectsUseCase,
) : UiScreen {
    override suspend fun show() {
        try {
            val projects = getAllProjectsUseCases.getAllProjects()
            if (projects.isNotEmpty()) {
                printer.printTitle("Project: ")
                projects.forEachIndexed { index, project ->
                    printer.printInfoLine(
                        """
                        ${index + 1}.
                        - Made by: ${project.creatorUserID}
                        - Name: ${project.title}
                        - Description: ${project.description}
                        - Creation Date: ${project.createdAt}
                        - Update Date: ${project.updatedAt}
                    """.trimIndent()
                    )
                }
            } else {
                printer.printError("No projects found.")
            }
        } catch (e: Exception) {
            printer.printError("Failed to retrieve projects: ${e.message}")
        }
    }
}
