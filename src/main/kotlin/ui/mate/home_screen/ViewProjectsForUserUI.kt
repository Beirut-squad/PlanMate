package org.example.ui.mate.home_screen

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectsForUserByIdUseCase
import org.example.models.Project
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectForMateUI
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ViewProjectsForUserUI : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase by inject()
    private val getProjectsForUserById: GetProjectsForUserByIdUseCase by inject()
    override suspend fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()
        val user = currentUserResult

        if (user == null) {
            viewer.printError("No user found")
            return
        }
        try {
            val userProjectsResult = getProjectsForUserById.getProjectForUserById(user.id)
            if (projects.isNotEmpty()) {
                viewer.printTitle("Project For User: ${user.name}")
                viewer.printOptions(
                    projects.map { it.name }
                )
                viewer.printTitle("Select a project to view details:")
                handleProjectSelection(projects)
            } else {
                viewer.printError("No project found for the current user.")
            }
        } catch (e: Exception) {
            viewer.printError("${e.message}")
        }
    }

    private suspend fun handleProjectSelection(projects: List<Project>) {
        var isRunning = true
        while (isRunning) {
            val input = viewer.readIntInput("Enter project number (or any number to exit):")
            handleUserInput(input, projects).also { isRunning = it }
        }
    }

    private suspend fun handleUserInput(input: Int?, projects: List<Project>): Boolean {
        return when {
            input == null -> {
                viewer.printError("Please enter a valid number.")
                true
            }

            input in 1..projects.size -> {
                handleProjectSelectionById(projects[input - 1].id)
                false
            }

            else -> {
                viewer.printGoodbyeMessage("Goodbye")
                MateHomeUI().show()
                true
            }
        }
    }

    private suspend fun handleProjectSelectionById(projectId: UUID) {
        ViewProjectForMateUI(
            projectId,
        ).show()
    }
}