package ui.view.user.admin.project

import ui.common.exception.NullInputException
import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.model.TaskState
import domain.useCase.state.DeleteStateUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteProjectStateUi(
    private val project: Project, private val taskState: TaskState
) : UiScreen, KoinComponent {
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val deleteStateUseCase: DeleteStateUseCase by inject()

    override suspend fun show() {
        printer.printTitle("Confirm deletion of state '${taskState.name}': Y/N")
        when (reader.readInput()?.uppercase()) {
            "Y" -> deleteState()
            "N" -> return
            else -> throw NullInputException()
        }

    }

    private suspend fun deleteState() {
        executor.tryToExecute(
            action = {
                deleteStateUseCase.deleteState(project, taskState)
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }
}