package ui.view.user.mate

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.useCase.authentication.GetCurrentUserUseCase
import domain.useCase.project.GetUserProjectsByIdUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.UiScreen
import ui.view.project.ProjectMateUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.common.Reader
import java.util.*

class UserProjectsUi : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val getCurrentLoggedInUserUseCase: GetCurrentUserUseCase by inject()
    private val getUserProjectsByIdUseCase: GetUserProjectsByIdUseCase by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    override suspend fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()

        executor.tryToExecute(
            action = {
                getUserProjectsByIdUseCase.getProjectForUserById(currentUserResult.id)
            },
            onError = {
                handler.printHandledError(it)
            },
            onSuccess = { projects ->
                if (projects.isNotEmpty()) {
                    printer.printTitle("Project For User: ${currentUserResult.name}")
                    printer.printOptions(
                        projects.map { it.title }
                    )
                    printer.printTitle("Select a project to view details:")
                    handleProjectSelection(projects)
                } else {
                    printer.printError("No project found for the current user.")
                }
            }
        )
    }

    private suspend fun handleProjectSelection(projects: List<Project>) {
        var isRunning = true
        while (isRunning) {
            printer.printOption("Enter project number (or any number to Go  Back):")
            val input = reader.readInt()
            handleUserInput(input, projects).also { isRunning = it }
        }
    }

    private suspend fun handleUserInput(input: Int?, projects: List<Project>): Boolean {
        return when (input) {
            null -> {
                printer.printError("Please enter a valid number.")
                true
            }

            in 1..projects.size -> {
                handleProjectSelectionById(projects[input - 1].id)
                false
            }

            else -> {
                false
            }
        }
    }

    private suspend fun handleProjectSelectionById(projectId: UUID) {
        ProjectMateUi(
            projectId,
        ).show()
    }
}