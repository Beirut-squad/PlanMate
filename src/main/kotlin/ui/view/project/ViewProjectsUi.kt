package ui.view.project

import domain.exception.handler.SafeExecutor
import domain.use_case.project.GetAllProjectsUseCase
import domain.exception.handler.ExceptionHandler
import ui.components.Printer
import ui.components.UiScreen

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
                        - Creation Date: ${project.createdAt}
                        - Update Date: ${project.updatedAt}
                    """.trimIndent()
                    )
                }
            }
        )
    }
}
