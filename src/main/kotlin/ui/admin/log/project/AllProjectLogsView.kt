package org.example.ui.admin.log.project

import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class AllProjectLogsView(
    private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val viewer: Viewer,
) : UiScreen, DisplayProjectLog(getUserByIdUseCase, viewer) {
    override suspend fun show() {
        getAllProjectLogsUseCase.getAllProjectLogs()
            .forEachIndexed { index, projectLog ->
                displayProjectLog(index, projectLog)
            }
    }
}