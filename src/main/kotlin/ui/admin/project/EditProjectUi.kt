package org.example.ui.admin.project

import domain.exception.handler.ExceptionHandler
import domain.model.Project
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.EditProjectDescriptionUseCase
import domain.use_case.project.EditProjectNameUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen

class EditProjectUi(
    private val printer: Printer,
    private val reader: Reader,
    private val editProjectNameUseCase: EditProjectNameUseCase,
    private val editProjectDescriptionUseCase: EditProjectDescriptionUseCase,
    private val currentUserUseCase: GetCurrentUserUseCase,
    private val exceptionHandler: ExceptionHandler,
) : UiScreen {

    lateinit var project: Project

    override suspend fun show() {
        var isContinueProcess = true
        while (isContinueProcess) {
            displayMenu()
            exceptionHandler.runSafely {
                val option = reader.readInt()
                when (option) {
                    1 -> editProjectName()
                    2 -> editProjectDescription()
                    3 -> isContinueProcess = false
                }
            }
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
        editProjectNameUseCase.editProject(project, newProjectName, editorUserId)
    }

    private suspend fun editProjectDescription() {
        printer.printPlainText("Edit Project Description: ", withNewLine = false)
        val newProjectDescription = reader.readInput()
        val editorUserId = currentUserUseCase.getCurrentUser().id
        editProjectDescriptionUseCase.editProject(project, newProjectDescription, editorUserId)

    }
}