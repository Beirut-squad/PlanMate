package org.example.ui.mate

import data.csv.model.Project
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
    override suspend fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()

        if (currentUserResult == null) {
            printer.printError("No user found")
            return
        }
        try {
            val projects = getUserProjectsByIdUseCase.getProjectForUserById(currentUserResult.id)
            if (projects.isNotEmpty()) {
                printer.printTitle("Project For User: ${currentUserResult.name}")
                printer.printOptions(
                    projects.map { it.name }
                )
                printer.printTitle("Select a project to view details:")
                handleProjectSelection(projects)
            } else {
                printer.printError("No project found for the current user.")
            }
        } catch (e: Exception) {
            printer.printError("${e.message}")
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