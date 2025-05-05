package org.example.ui.mate.home_screen

import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.screens.ViewProjectLogsScreen
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectsForUserScreen

class MateHomeScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectsForUserScreen : ViewProjectsForUserScreen,
    private val viewProjectLogsScreen: ViewProjectLogsScreen
) : UiScreen {
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate")

        var running = true
        while (running) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions("View Projects","View Project Logs", "Exit")

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectsScreen()
                    running = false
                }

                2 -> {
                    goToViewProjectLogsScreen()
                    running = false
                }

                3 -> {
                    viewer.printGoodbyeMessage("Goodbye")
                    break
                }
            }
        }
    }

    private fun goToViewProjectsScreen() {
        viewProjectsForUserScreen.show()
    }
    private fun goToViewProjectLogsScreen() {
        viewProjectLogsScreen.show()
    }
}