package org.example.ui.admin.project

import org.example.logic.use_cases.project_manegment.CreateProjectUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.*

class CreateNewProjectScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val createProjectUseCase: CreateProjectUseCase

) : UiScreen {
    override suspend fun show() {
        viewer.printTitle("Let's create a project")
        viewer.printInfoLine("Write your project name")
        val name = reader.readInput()
        viewer.printOptions("Tell me more about description of your project")
        val description = reader.readInput()
        viewer.printOptions("What about project state")
        val stateOfProject = reader.readInput()
        createProjectUseCase.createProject(
            creatorUserID = UUID.randomUUID(),
            name = name.toString(),
            description = description.toString(),
            stateNames = listOf(stateOfProject.toString())
        )
    }
}