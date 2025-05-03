package org.example.ui.home_screens

import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.home_screen.CreateNewProjectScreen
import ui.Viewer

class HomeScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectsScreen: ViewProjectsScreen,
    private val createNewProjectScreen: CreateNewProjectScreen,
    private val viewProjectLogsScreen: ViewProjectLogsScreen,
) : UiScreen {
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate")

        var running = true
        while (running) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions("View Your Project", "Create a New Project", "View Project Logs", "Exit")

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
                    goToViewProjectLogsScreen()
                    running = false
                }

                4 -> {
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

    private fun goToViewProjectLogsScreen() {
        viewProjectLogsScreen.show()
    }
}