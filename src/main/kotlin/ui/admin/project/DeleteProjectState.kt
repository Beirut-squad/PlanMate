package org.example.ui.admin.project

import org.example.logic.exceptions.ErrorHandler
import org.example.logic.exceptions.NullInputException
import org.example.logic.use_cases.state_usecase.DeleteStateUseCase
import org.example.models.State
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.UUID

class DeleteProjectState(
    private val errorHandler: ErrorHandler,
    private val viewer: Viewer,
    private val reader: Reader,
    private val state: State,
    private val projectId: UUID,
    private val deleteStateUseCase: DeleteStateUseCase,
) : UiScreen {
    override fun show() {
        viewer.printTitle("Confirm deletion of state '${state.name}': Y/N")
        when (reader.readInput()?.uppercase()) {
            "Y" -> deleteState()
            "N" -> return
            else -> throw NullInputException()
        }

    }

    private fun deleteState() {
        deleteStateUseCase.deleteState(projectId, state).onFailure {
            errorHandler.handle(it)
        }
    }
}