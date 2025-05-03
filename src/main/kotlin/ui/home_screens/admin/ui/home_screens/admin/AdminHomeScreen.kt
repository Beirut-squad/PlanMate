package org.example.ui.home_screens.admin.ui.home_screens.admin

import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.home_screen.CreateNewProjectScreen
import org.example.ui.home_screens.ViewProjectLogsScreen
import org.example.ui.home_screens.ViewProjectsScreen
import ui.Viewer

class AdminHomeScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectsScreen: ViewProjectsScreen,
    private val createNewProjectScreen: CreateNewProjectScreen,
) : UiScreen {
    override fun show() {

        var running = true
        while (running) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions("View Current Projects", "Create a New Project", "Exit")

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectScreen()
                    running = false
                }

                2 -> {
                    goToCreateNewProjectScreen()
                    running = false
                }

                3 -> {
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
}