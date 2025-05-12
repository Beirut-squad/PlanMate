package ui.admin.log.task

import domain.exception.handler.SafeExecutor
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllTaskLogsUseCase
import org.example.core.domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class TaskLogsUi(
    private val getAllTaskLogsUseCase: GetAllTaskLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
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