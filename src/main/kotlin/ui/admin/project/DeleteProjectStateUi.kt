package org.example.ui.admin.project

import org.example.data.model.Project
import org.example.data.model.State
import domain.exception.ErrorHandler
import domain.exception.NullInputException
import domain.use_case.state.DeleteStateUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteProjectStateUi(
    private val project: Project,
    private val state: State
) : UiScreen, KoinComponent {
    private val errorHandler: ErrorHandler by inject()
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
        try {
            deleteStateUseCase.deleteState(project, state)
        }catch (e:Exception){
            printer.printError("${e.message}")
        }
    }
}