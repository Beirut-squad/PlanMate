package org.example.ui.home_screens.mate.ui.home_screens.mate

import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.home_screen.CreateNewProjectScreen
import org.example.ui.home_screens.ViewProjectLogsScreen
import org.example.ui.home_screens.ViewProjectsScreen
import ui.Viewer

class MateHomeScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectLogsScreen: ViewProjectLogsScreen,
) : UiScreen {
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate")

        var running = true
        while (running) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions("View Project Logs", "Exit")

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectLogsScreen()
                    running = false
                }

                2 -> {
                    viewer.printGoodbyeMessage("Goodbye")
                    break
                }
            }
        }
    }

    private fun goToViewProjectLogsScreen() {
        viewProjectLogsScreen.show()
    }
}