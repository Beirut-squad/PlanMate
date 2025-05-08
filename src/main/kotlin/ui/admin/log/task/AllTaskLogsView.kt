package ui.admin.log.task

import logic.use_cases.log.GetAllTaskLogsUseCase
import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class AllTaskLogsView(
    private val getAllTaskLogsUseCase: GetAllTaskLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val viewer: Viewer,
) : UiScreen, DisplayTaskLog(getUserByIdUseCase, viewer) {

    override suspend fun show() {
        getAllTaskLogsUseCase.getAllTaskLogs()
            .forEachIndexed { index, taskLog ->
                displayTaskLog(index, taskLog)
            }
    }
}