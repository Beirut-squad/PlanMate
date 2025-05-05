package org.example.ui.mate.home_screen

import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.screens.ViewProjectLogsUI
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectsForUserUI

class MateHomeUI(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectsForUserUI : ViewProjectsForUserUI,
    private val viewProjectLogsUI: ViewProjectLogsUI
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
        viewProjectsForUserUI.show()
    }
    private fun goToViewProjectLogsScreen() {
        viewProjectLogsUI.show()
    }
}