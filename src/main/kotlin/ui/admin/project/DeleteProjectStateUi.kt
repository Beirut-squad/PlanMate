package org.example.ui.admin.project

import domain.exception.NullInputException
import domain.exception.handler.ExceptionHandler
import domain.model.Project
import domain.model.State
import domain.use_case.state.DeleteStateUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteProjectStateUi(
    private val project: Project, private val state: State
) : UiScreen, KoinComponent {
    private val exceptionHandler: ExceptionHandler by inject()
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val deleteStateUseCase: DeleteStateUseCase by inject()

    override suspend fun show() {
        printer.printTitle("Confirm deletion of state '${state.name}': Y/N")
        when (reader.readInput()?.uppercase()) {
            "Y" -> deleteState()
            "N" -> return
            else -> throw NullInputException()
        }

    }

    private suspend fun deleteState() {
        exceptionHandler.runSafely {
            deleteStateUseCase.deleteState(project, state)
        }
    }
}