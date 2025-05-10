package ui.admin.log.task

import domain.exception.handler.ExceptionHandler
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllTaskLogsUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class TaskLogsUi(
    private val getAllTaskLogsUseCase: GetAllTaskLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
    private val exceptionHandler: ExceptionHandler,
) : UiScreen, TaskLogUi(getUserByIdUseCase, printer, exceptionHandler) {

    override suspend fun show() {
        exceptionHandler.tryCatchingAsync(
            action = {
                getAllTaskLogsUseCase.getAllTaskLogs()
                    .forEachIndexed { index, taskLog ->
                        displayTaskLog(index, taskLog)
                    }
            }
        )
    }
}