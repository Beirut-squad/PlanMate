package org.example.ui.mate

import org.example.ui.common.authentication.StartUpMenuUi
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Printer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MateUi : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val userProjectsUI: UserProjectsUi by inject()
    private val startUpMenuUi : StartUpMenuUi by inject()

    override suspend fun show() {
        printer.printTitle("Welcome to Plan Mate")

        while (true) {

            printer.printInfoLine("Choose an option:")
            printer.printOptions(
                "View Projects",
                "Exit"
            )

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectsScreen()
                }

                2 -> {
                    printer.printGoodbyeMessage("Goodbye")
                    goToAuthenticationMainScreen()
                }
            }
        }
    }

    private suspend fun goToViewProjectsScreen() {
        userProjectsUI.show()
    }
    private suspend fun goToAuthenticationMainScreen() {
        startUpMenuUi.show()
    }
}