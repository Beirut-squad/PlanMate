package org.example.ui.authentication_screens

import org.example.ui.Reader
import org.example.ui.UiScreen
import ui.Viewer

class AuthenticationMainScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val registerScreen: RegisterScreen,
    private val loginScreen: LoginScreen
) : UiScreen {
    override fun show() {
        viewer.printTitle("Welcome to Plan Mate, what would you like to do?")

        var running = true
        while (running) {
            viewer.printOptions(
                "Register",
                "Login"
            )

            val input = reader.readInt()
            when (input) {
                1 -> {
                    goToRegisterScreen()
                    running = false
                }

                2 -> {
                    goToLoginScreen()
                    running = false
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
        loginScreen.show()
    }

}