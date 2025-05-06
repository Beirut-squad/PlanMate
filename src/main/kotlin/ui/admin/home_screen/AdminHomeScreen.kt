package org.example.ui.admin.home_screen

import org.example.ui.admin.log.AllProjectsLogsView
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.admin.project.CreateNewProjectScreen
import org.example.ui.admin.project.ViewProjectsScreen
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectsForUserUI

class AdminHomeScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectsForUserUI : ViewProjectsForUserUI,
    private val createNewProjectScreen: CreateNewProjectScreen,
    private val allProjectsLogsView: AllProjectsLogsView
) : UiScreen {
    override fun show() {

        while (true) {
            viewer.printInfoLine("Choose an option:")
            viewer.printOptions(
                "View Current Projects",
                "Create a New Project",
                "Show all project logs",
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
                    goToViewAllLogsScreen()
                }

                4 -> {
                    viewer.printGoodbyeMessage("Goodbye")
                    break
                }

            }
        }
    }

    private fun goToViewProjectScreen() {
        viewProjectsForUserUI.show()
    }

    private fun goToCreateNewProjectScreen() {
        createNewProjectScreen.show()
    }

    private fun goToViewAllLogsScreen() {
        allProjectsLogsView.show()
    }
}