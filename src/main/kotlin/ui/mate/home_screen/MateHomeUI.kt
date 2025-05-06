package org.example.ui.mate.home_screen

import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.screens.ViewProjectLogsUI
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectsForUserUI
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MateHomeUI() : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val viewProjectsForUserUI: ViewProjectsForUserUI by inject()
    private val authenticationMainScreen : AuthenticationMainScreen by inject()
    private val viewProjectLogsUI: ViewProjectLogsUI by inject()
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate")

        var running = true
        while (running) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions("View Projects", "View Project Logs", "Exit")

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
                    goToAuthenticationMainScreen()
                }
            }
        }
    }

    private fun goToViewProjectsScreen() {
        viewProjectsForUserUI.show()
    }
    private fun goToAuthenticationMainScreen() {
        authenticationMainScreen.show()
    }

    private fun goToViewProjectLogsScreen() {
        viewProjectLogsUI.show()
    }
}