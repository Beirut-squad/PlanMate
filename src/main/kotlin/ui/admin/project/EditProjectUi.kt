package org.example.ui.admin.project

import data.csv.model.Project
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
) : UiScreen {

    lateinit var project: Project

    override suspend fun show() {
        var isContinueProcess = true
        while (isContinueProcess) {
            displayMenu()
            val option = reader.readInt()
            when (option) {
                1 -> editProjectName()
                2 -> editProjectDescription()
                3 -> isContinueProcess = false
            }
        }
    }

    private fun displayMenu() {
        printer.printOptions(
            "Edit project name",
            "Edit project description",
            "Return"
        )
        printer.printTitle("Choose what you want to change in project.")
    }

    private suspend fun editProjectName() {
        printer.printPlainText("Edit Project Name: ", withNewLine = false)
        val newProjectName = reader.readInput()
        try {
            val editorUserId = currentUserUseCase.getCurrentUser()?.id ?: return
            editProjectNameUseCase.editProject(project, newProjectName, editorUserId)
        } catch (e: Exception) {
            printer.printError("${e.message}")
        }

    }

    private suspend fun editProjectDescription() {
        printer.printPlainText("Edit Project Description: ", withNewLine = false)
        val newProjectDescription = reader.readInput()
        try {
            val editorUserId = currentUserUseCase.getCurrentUser()?.id ?: return
            editProjectDescriptionUseCase.editProject(project, newProjectDescription, editorUserId)
        } catch (e: Exception) {
            printer.printError("${e.message}")
        }
    }
}