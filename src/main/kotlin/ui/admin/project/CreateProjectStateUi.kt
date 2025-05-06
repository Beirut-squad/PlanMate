package org.example.ui.admin.project

import org.example.logic.use_cases.state_usecase.CreateStateUseCase
import org.example.models.Project
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateProjectStateUi(
    private val project: Project
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val createStateUseCase: CreateStateUseCase by inject()
    override fun show() {
        try {
            viewer.printTitle("Create State")
            viewer.printTitle("Please, Write your state name")
            val stateName = reader.readInput().toString()
            createStateUseCase.createState(name = stateName, project = project)
        } catch (e: Exception) {
            viewer.printError("Error while creating state")
        }
    }
}