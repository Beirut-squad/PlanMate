package org.example.ui.admin.log.project

import domain.exception.handler.ExceptionHandler
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllProjectLogsUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class ProjectLogsUi(
    private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
    private val exceptionHandler: ExceptionHandler,
) : UiScreen, ProjectLogUi(getUserByIdUseCase, printer, exceptionHandler) {
    override suspend fun show() {
        exceptionHandler.tryCatchingAsync(
            action = {
                getAllProjectLogsUseCase.getAllProjectLogs()
                    .forEachIndexed { index, projectLog ->
                        displayProjectLog(index, projectLog)
                    }
            }
        )
    }
}