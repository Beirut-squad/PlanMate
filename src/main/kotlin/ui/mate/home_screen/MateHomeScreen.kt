package org.example.ui.mate.home_screen

import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.screens.ViewProjectLogsScreen
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectForUserScreen

class MateHomeScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectsForUserScreen : ViewProjectForUserScreen,
) : UiScreen {
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate")

        var running = true
        while (running) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions("View Projects", "Exit")

            val option = reader.readInt()
            when (option) {
                1 -> {

                    goToViewProjectsScreen()
                    running = false
                }

                2 -> {
                    viewer.printGoodbyeMessage("Goodbye")
                    break
                }
            }
        }
    }

    private fun goToViewProjectsScreen() {
        viewProjectsForUserScreen.show()
    }
}