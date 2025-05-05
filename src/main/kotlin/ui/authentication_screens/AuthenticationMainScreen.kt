package org.example.ui.authentication_screens

import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class AuthenticationMainScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val registerScreen: RegisterScreen,
    private val loginScreen: LoginScreen
) : UiScreen {
    override suspend fun show() {
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

    private suspend fun goToRegisterScreen() {
        registerScreen.show()
    }

    private suspend fun goToLoginScreen() {
        loginScreen.show()
    }

}