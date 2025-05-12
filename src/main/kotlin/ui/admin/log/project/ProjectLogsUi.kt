package org.example.ui.admin.log.project

import domain.exception.handler.SafeExecutor
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllProjectLogsUseCase
import org.example.core.domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen

class ProjectLogsUi(
    private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
) : UiScreen, ProjectLogUi(getUserByIdUseCase, printer) {
    override suspend fun show() {
        executor.tryToExecute(
            action = {
                getAllProjectLogsUseCase.getAllProjectLogs()
            },
            onSuccess = { projectLogs ->
                projectLogs.forEachIndexed { index, projectLog ->
                    displayProjectLog(index, projectLog)
                }
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }
}