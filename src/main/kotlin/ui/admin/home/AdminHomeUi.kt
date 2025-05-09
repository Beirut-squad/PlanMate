package org.example.ui.admin.home_screen

import org.example.ui.admin.log.project.AllProjectLogsView
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.admin.project.CreateNewProjectUi
import org.example.ui.admin.project.ViewProjectsUi
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AdminHomeUi(

) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val viewProjectsUi: ViewProjectsUi by inject()
    private val createNewProjectUi: CreateNewProjectUi by inject()
    private val allProjectLogsView: AllProjectLogsView by inject()
    private val allTaskLogsView: AllProjectLogsView by inject()

    override suspend fun show() {

        while (true) {
            viewer.printInfoLine("Choose an option:")
            viewer.printOptions(
                "View Current Projects",
                "Create a New Project",
                "Show all project logs",
                "Show all task logs",
                "Exit"
            )

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectScreen()
                }

                2 -> {
                    goToCreateNewProjectScreen()
                }

                3 -> {
                    goToViewAllProjectLogsScreen()
                }

                4 -> {
                    goToViewAllTaskLogsScreen()
                }

                5 -> {
                    viewer.printGoodbyeMessage("Goodbye")
                    break
                }

            }
        }
    }

    private suspend fun goToViewProjectScreen() {
        viewProjectsUi.show()
    }

    private suspend fun goToCreateNewProjectScreen() {
        createNewProjectUi.show()
    }

    private suspend fun goToViewAllProjectLogsScreen() {
        allProjectLogsView.show()
    }

    private suspend fun goToViewAllTaskLogsScreen() {
        allTaskLogsView.show()
    }
}