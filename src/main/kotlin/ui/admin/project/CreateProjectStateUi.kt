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
    private val ProjectId: UUID
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val addStateToProjectUseCase: AddStateToProjectUseCase by inject()
    private val createStateUseCase: CreateStateUseCase by inject()
    override fun show() {
        viewer.printTitle("Create Task State")
        viewer.printTitle("Please, Write your state name")
        val stateName = reader.readInput().toString()
        val stateCreation = createStateUseCase.createState(stateName)
        addStateToProjectUseCase.addStateToProject(projectId = ProjectId, state = stateCreation)
    }
}