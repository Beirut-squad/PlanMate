package ui.view.user.admin.log.project

import ui.common.exception.handler.ExceptionHandler
import ui.common.exception.handler.SafeExecutor
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllProjectLogsUseCase
import ui.common.Printer
import ui.common.UiScreen

class ProjectLogsUi(
    private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase,
    getUserByIdUseCase: GetUserByIdUseCase,
    printer: Printer,
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