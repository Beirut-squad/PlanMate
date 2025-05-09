package ui.admin.log.task

import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllTaskLogsUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class TaskLogsUi(
    private val getAllTaskLogsUseCase: GetAllTaskLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
) : UiScreen, TaskLogUi(getUserByIdUseCase, printer) {

    override suspend fun show() {
        getAllTaskLogsUseCase.getAllTaskLogs()
            .forEachIndexed { index, taskLog ->
                displayTaskLog(index, taskLog)
            }
    }
}