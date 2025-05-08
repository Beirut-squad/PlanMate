package org.example.ui.admin.project

import logic.use_cases.state_usecase.EditStateUseCase
import org.example.models.Project
import org.example.models.State
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditProjectStateUi(
    private val project: Project,
    private val state: State
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val editStateUseCase: EditStateUseCase by inject()

    override fun show() {
        viewer.printTitle("Edit State")

        viewer.printTitle("Current state name: ${state.name}")
        viewer.printTitle("Enter new state name:")
        val newName = reader.readInput().toString()

        try {
            editStateUseCase.editState(state, newName, project)
            viewer.printTitle("State updated successfully to: $newName")
        } catch (e: Exception) {
            viewer.printError("${e.message}")
        }
    }

}