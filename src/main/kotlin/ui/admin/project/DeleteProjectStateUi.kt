package org.example.ui.admin.project

import org.example.logic.exceptions.ErrorHandler
import org.example.logic.exceptions.NullInputException
import org.example.logic.use_cases.state_usecase.DeleteStateUseCase
import org.example.models.Project
import org.example.models.State
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteProjectStateUi(
    private val project: Project,
    private val state: State
) : UiScreen, KoinComponent {
    private val errorHandler: ErrorHandler by inject()
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val deleteStateUseCase: DeleteStateUseCase by inject()

    override fun show() {
        viewer.printTitle("Confirm deletion of state '${state.name}': Y/N")
        when (reader.readInput()?.uppercase()) {
            "Y" -> deleteState()
            "N" -> return
            else -> throw NullInputException()
        }

    }

    private fun deleteState() {
        try {
            deleteStateUseCase.deleteState(project, state)
        }catch (e:Exception){
            viewer.printError("${e.message}")
        }
    }
}