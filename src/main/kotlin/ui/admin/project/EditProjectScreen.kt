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

    override fun show() {
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
        viewer.printOptions(
            "Edit project name",
            "Edit project description",
            "Return"
        )
        viewer.printTitle("Choose what you want to change in project.")
    }

    private fun editProjectName() {
        viewer.printPlainText("Edit Project Name: ", withNewLine = false)
        val newProjectName = reader.readInput()
        val editorUserId = currentLoggedInUserUseCase.getCurrentUser().getOrThrow()?.id ?: return
        editProjectNameUseCase.editProject(project, newProjectName, editorUserId)
    }

    private fun editProjectDescription() {
        viewer.printPlainText("Edit Project Description: ", withNewLine = false)
        val newProjectDescription = reader.readInput()
        val editorUserId = currentLoggedInUserUseCase.getCurrentUser().getOrThrow()?.id ?: return
        editProjectDescriptionUseCase.editProject(project, newProjectDescription, editorUserId)
    }

}