package ui.view.user.admin.log.task

import domain.exception.handler.SafeExecutor
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllTaskLogsUseCase
import domain.exception.handler.ExceptionHandler
import ui.components.Printer
import ui.components.UiScreen

class TaskLogsUi(
    private val getAllTaskLogsUseCase: GetAllTaskLogsUseCase,
    getUserByIdUseCase: GetUserByIdUseCase,
    printer: Printer,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
) : UiScreen, TaskLogUi(getUserByIdUseCase, printer) {

    override suspend fun show() {
        executor.tryToExecute(
            action = {
                getAllTaskLogsUseCase.getAllTaskLogs()
            },
            onSuccess = { taskLogs ->
                taskLogs.forEachIndexed { index, taskLog ->
                    displayTaskLog(index, taskLog)
                }
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }
}