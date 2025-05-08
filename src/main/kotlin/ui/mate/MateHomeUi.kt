package org.example.ui.mate

import org.example.ui.common.authentication.AuthenticationMainUi
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MateHomeUi : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val viewProjectsForUserUI: ViewProjectsForUserUi by inject()
    private val authenticationMainUi : AuthenticationMainUi by inject()

    override suspend fun show() {
        viewer.printTitle("Welcome to Plan Mate")

        while (true) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions(
                "View Projects",
                "Exit"
            )

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectsScreen()
                }

                2 -> {
                    viewer.printGoodbyeMessage("Goodbye")
                    goToAuthenticationMainScreen()
                }
            }
        }
    }

    private suspend fun goToViewProjectsScreen() {
        viewProjectsForUserUI.show()
    }
    private suspend fun goToAuthenticationMainScreen() {
        authenticationMainUi.show()
    }
}