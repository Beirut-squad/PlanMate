package org.example.ui.admin.project

import domain.exception.handler.SafeExecutor
import domain.use_case.project.CreateProjectUseCase
import org.example.core.domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen

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