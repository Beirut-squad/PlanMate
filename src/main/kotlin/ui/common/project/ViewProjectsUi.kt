package org.example.ui.common.project

import domain.exception.handler.ExceptionHandler
import domain.use_case.project.GetAllProjectsUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class ViewProjectsUi(
    private val printer: Printer,
    private val getAllProjectsUseCases: GetAllProjectsUseCase,
    private val exceptionHandler: ExceptionHandler,
) : UiScreen {
    override suspend fun show() {
        exceptionHandler.runSafely {
            getAllProjectsUseCases.getAllProjects()
        }.onSuccess { projects ->
            if (projects.isEmpty()) {
                printer.printError("No projects found.")
                return
            }
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
        }
    }
}
