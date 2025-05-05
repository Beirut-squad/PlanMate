package org.example.ui.admin.project

import org.example.logic.use_cases.project_manegment.CreateProjectUseCase
import org.example.models.State
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.*

class CreateNewProjectScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val createProjectUseCase: CreateProjectUseCase

) : UiScreen {
    override fun show() {
        viewer.printTitle("Let's create a project")
        viewer.printOption("Write your project name")
        val name = reader.readInput()
        viewer.printOption("Tell me more about description of your project")
        val description = reader.readInput()
        createProjectUseCase.run {
            createProject(
                creatorUserID = UUID.randomUUID(),
                name = name.toString(),
                description = description.toString(),
                stateNames = emptyList()
            )
        }
    }
}