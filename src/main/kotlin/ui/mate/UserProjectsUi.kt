package org.example.ui.mate

import domain.exception.handler.ExceptionHandler
import domain.model.Project
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.GetUserProjectsByIdUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.UiScreen
import org.example.ui.common.project.ProjectMateUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UserProjectsUi : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val getCurrentLoggedInUserUseCase: GetCurrentUserUseCase by inject()
    private val getUserProjectsByIdUseCase: GetUserProjectsByIdUseCase by inject()
    private val expectationHandle: ExceptionHandler by inject()

    override suspend fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()

        expectationHandle.runSafely {
            getUserProjectsByIdUseCase.getProjectForUserById(currentUserResult.id)
        }.onSuccess { projects ->
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
    }

    private suspend fun handleProjectSelection(projects: List<Project>) {
        var isRunning = true
        while (isRunning) {
            val input = printer.readIntInput("Enter project number (or any number to exit):")
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
                printer.printGoodbyeMessage("Goodbye")
                MateUi().show()
                true
            }
        }
    }

    private suspend fun handleProjectSelectionById(projectId: UUID) {
        ProjectMateUi(
            projectId,
        ).show()
    }
}