package org.example.ui.authentication_screens

import org.example.ui.Reader
import org.example.ui.UiScreen
import ui.Viewer

class AuthenticationMainScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val registerScreen: RegisterScreen
) : UiScreen {
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate, what would you like to do?")

        while (true) {
            viewer.printOptions(
                "Register",
                "Login"
            )


            when (reader.readInt()) {
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
        registerScreen.show()
    }

    private fun goToLoginScreen() {
        // TODO
    }

}