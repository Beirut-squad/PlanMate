package org.example.ui.admin.project

import domain.model.Project
import domain.model.State
import domain.use_case.state.EditStateUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditProjectStateUi(
    private val project: Project,
    private val state: State
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val editStateUseCase: EditStateUseCase by inject()

    override suspend fun show() {
        printer.printTitle("Edit State")

        printer.printTitle("Current state name: ${state.name}")
        printer.printTitle("Enter new state name:")
        val newName = reader.readInput().toString()

        try {
            editStateUseCase.editState(state, newName, project)
            printer.printTitle("State updated successfully to: $newName")
        } catch (e: Exception) {
            printer.printError("${e.message}")
        }
    }

}