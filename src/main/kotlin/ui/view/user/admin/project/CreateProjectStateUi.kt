package ui.view.user.admin.project

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.use_case.state.CreateStateUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Reader
import ui.common.UiScreen
import ui.common.Printer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateProjectStateUi(
    private val project: Project
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val createStateUseCase: CreateStateUseCase by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    override suspend fun show() {
        printer.printTitle("Create State")
        printer.printInfoLine("Please, Write your state name")
        executor.tryToExecute(
            action = {
                val stateName = reader.readInput().toString()
                createStateUseCase.createState(name = stateName, project = project)
            },
            onError = {
                handler.printHandledError(it)
            },
        )
    }
}