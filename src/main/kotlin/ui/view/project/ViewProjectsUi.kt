package ui.view.project

import ui.common.exception.handler.SafeExecutor
import domain.useCase.project.GetAllProjectsUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.UiScreen
import ui.extensions.formatDateTime

class ViewProjectsUi(
    private val printer: Printer,
    private val getAllProjectsUseCases: GetAllProjectsUseCase,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
) : UiScreen {
    override suspend fun show() {
        executor.tryToExecute(
            action = {
                getAllProjectsUseCases.getAllProjects()
            },
            onError = {
                handler.printHandledError(it)
            },
            onSuccess = { projects ->
                if (projects.isEmpty()) {
                    printer.printError("No projects found.")
                    return@tryToExecute
                }
                printer.printTitle("Project: ")
                projects.forEachIndexed { index, project ->
                    printer.printInfoLine(
                        """
                        ${index + 1}.
                        - Made by: ${project.creatorUserID}
                        - Name: ${project.title}
                        - Description: ${project.description}
                        - Creation Date: ${project.createdAt.formatDateTime()}
                        - Update Date: ${project.updatedAt.formatDateTime()}
                    """.trimIndent()
                    )
                }
            }
        )
    }
}
