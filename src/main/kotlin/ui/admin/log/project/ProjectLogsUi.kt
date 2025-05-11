package org.example.ui.admin.log.project

import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllProjectLogsUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class ProjectLogsUi(
    private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
) : UiScreen, ProjectLogUi(getUserByIdUseCase, printer) {
    override suspend fun show() {
        getAllProjectLogsUseCase.getAllProjectLogs()
            .forEachIndexed { index, projectLog ->
                displayProjectLog(index, projectLog)
            }
    }
}