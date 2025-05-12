package ui.view.user.admin.project

import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.use_case.project.CreateProjectUseCase
import ui.components.Printer
import ui.components.Reader
import ui.components.UiScreen

class CreateProjectUi(
    private val printer: Printer,
    private val reader: Reader,
    private val createProjectUseCase: CreateProjectUseCase,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
    ) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Let's create a project")
        printer.printInfoLine("Write your project title")
        val name = reader.readInput()
        printer.printOption("Tell me more about description of your project")
        val description = reader.readInput()
        executor.tryToExecute(
            action = {
                createProjectUseCase.createProject(
                    name = name.toString(),
                    description = description.toString(),
                    stateNames = emptyList(),
                )
            },
            onError = {
                handler.printHandledError(it)
            }
        )

    }
}