package ui.view.user.admin.project

import domain.exception.handler.SafeExecutor
import domain.model.Project
import domain.model.State
import domain.use_case.state.EditStateUseCase
import domain.exception.handler.ExceptionHandler
import ui.components.Printer
import ui.components.Reader
import ui.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditProjectStateUi(
    private val project: Project,
    private val state: State
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val editStateUseCase: EditStateUseCase by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    override suspend fun show() {
        printer.printTitle("Edit State")

        printer.printTitle("Current state name: ${state.name}")
        printer.printTitle("Enter new state name:")
        val newName = reader.readInput().toString()

        executor.tryToExecute(
            action = {
                editStateUseCase.editState(state, newName, project)
                printer.printTitle("State updated successfully to: $newName")
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }

}