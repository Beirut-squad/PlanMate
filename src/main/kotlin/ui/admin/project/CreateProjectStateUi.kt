package org.example.ui.admin.project

import org.example.logic.use_cases.project_manegment.AddStateToProjectUseCase
import org.example.logic.use_cases.state_usecase.CreateStateUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class CreateProjectStateUi(
    private val projectId: UUID
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val addStateToProjectUseCase: AddStateToProjectUseCase by inject()
    private val createStateUseCase: CreateStateUseCase by inject()
    override fun show() {
        try {
            viewer.printTitle("Create State")
            viewer.printTitle("Please, Write your state name")
            val stateName = reader.readInput().toString()
            val stateCreation = createStateUseCase.createState(stateName, projectId)
            addStateToProjectUseCase.addStateToProject(projectId = projectId, state = stateCreation.getOrThrow())
        } catch (e: Exception) {
            viewer.printError("Error while creating state")
        }
    }
}