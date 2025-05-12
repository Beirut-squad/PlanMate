package org.example.ui.admin.project

import domain.exception.handler.SafeExecutor
import domain.model.Project
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.EditProjectDescriptionUseCase
import domain.use_case.project.EditProjectTitleUseCase
import org.example.core.domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen

class EditProjectUi(
    private val printer: Printer,
    private val reader: Reader,
    private val editProjectTitleUseCase: EditProjectTitleUseCase,
    private val editProjectDescriptionUseCase: EditProjectDescriptionUseCase,
    private val currentUserUseCase: GetCurrentUserUseCase,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
) : UiScreen {

    lateinit var project: Project

    override suspend fun show() {
        var isContinueProcess = true
        while (isContinueProcess) {
            displayMenu()
            executor.tryToExecute(
                action = {
                    val option = reader.readInt()
                    when (option) {
                        1 -> editProjectName()
                        2 -> editProjectDescription()
                        3 -> isContinueProcess = false
                    }
                },
                onError = {
                    handler.printHandledError(it)
                }
            )
        }
    }

    private fun displayMenu() {
        printer.printOptions(
            "Edit project name", "Edit project description", "Return"
        )
        printer.printTitle("Choose what you want to change in project.")
    }

    private suspend fun editProjectName() {
        printer.printPlainText("Edit Project Name: ", withNewLine = false)
        val newProjectName = reader.readInput()
        val editorUserId = currentUserUseCase.getCurrentUser().id
        editProjectTitleUseCase.editProject(project, newProjectName, editorUserId)
    }

    private suspend fun editProjectDescription() {
        printer.printPlainText("Edit Project Description: ", withNewLine = false)
        val newProjectDescription = reader.readInput()
        val editorUserId = currentUserUseCase.getCurrentUser().id
        editProjectDescriptionUseCase.editProject(project, newProjectDescription, editorUserId)

    }
}