package org.example.ui.authentication_screens

import org.example.ui.Reader
import org.example.ui.UiScreen
import ui.Viewer

class AuthenticationMainScreen(
    private val reader: Reader,
    private val viewer: Viewer
) : UiScreen {
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate, what would you like to do?")

        while (true) {
            viewer.printOptions(
                "Register",
                "Login"
            )
            val input = reader.readInt()

            when (input) {
                1 -> {
                    goToRegisterScreen()
                    break
                }

                2 -> {
                    goToLoginScreen()
                    break
                }

                else -> {
                    viewer.printError("Invalid option")
                }
            }
        }
    }

    private fun goToRegisterScreen() {
        // TODO
    }

    private fun goToLoginScreen() {
        // TODO
    }

}