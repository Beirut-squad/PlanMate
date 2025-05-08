package org.example.ui.admin.home_screen

import org.example.ui.admin.log.project.AllProjectLogsView
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.admin.project.CreateNewProjectScreen
import org.example.ui.admin.project.ViewProjectsScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.admin.log.task.AllTaskLogsView

class AdminHomeScreen(

) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val viewProjectsScreen: ViewProjectsScreen by inject()
    private val createNewProjectScreen: CreateNewProjectScreen by inject()
    private val allProjectLogsView: AllProjectLogsView by inject()
    private val allTaskLogsView: AllTaskLogsView by inject()
    override fun show() {

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

    private fun goToViewProjectScreen() {
        viewProjectsScreen.show()
    }

    private fun goToCreateNewProjectScreen() {
        createNewProjectScreen.show()
    }

    private fun goToViewAllProjectLogsScreen() {
        allProjectLogsView.show()
    }

    private fun goToViewAllTaskLogsScreen() {
        allTaskLogsView.show()
    }
}