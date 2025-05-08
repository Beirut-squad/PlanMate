package org.example.ui.admin.project

import logic.use_cases.project_manegment.EditProjectDescriptionUseCase
import logic.use_cases.project_manegment.EditProjectNameUseCase
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.models.Project
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class EditProjectScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val editProjectNameUseCase: EditProjectNameUseCase,
    private val editProjectDescriptionUseCase: EditProjectDescriptionUseCase,
    private val currentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
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
        viewer.printOptions("1- Edit project name")
        viewer.printOptions("2- Edit project description")
        viewer.printOptions("3- Return")
        viewer.printTitle("Choose what you want to change in project.")
    }

    private suspend fun editProjectName() {
        viewer.printPlainText("Edit Project Name: ", withNewLine = false)
        val newProjectName = reader.readInput()
        try {
            val editorUserId = currentLoggedInUserUseCase.getCurrentUser()?.id ?: return
            editProjectNameUseCase.editProject(project, newProjectName, editorUserId)
        } catch (e: Exception) {
            viewer.printError("${e.message}")
        }

    }

    private suspend fun editProjectDescription() {
        viewer.printPlainText("Edit Project Description: ", withNewLine = false)
        val newProjectDescription = reader.readInput()
        try {
            val editorUserId = currentLoggedInUserUseCase.getCurrentUser()?.id ?: return
            editProjectDescriptionUseCase.editProject(project, newProjectDescription, editorUserId)
        } catch (e: Exception) {
            viewer.printError("${e.message}")
        }
    }
}