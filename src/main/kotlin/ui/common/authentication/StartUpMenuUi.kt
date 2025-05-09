package org.example.ui.common.authentication

import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Printer

class StartUpMenuUi(
    private val reader: Reader,
    private val printer: Printer,
    private val registerUi: RegisterUi,
    private val loginUi: LoginUi
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Welcome to Plan Mate, what would you like to do?")

        while (true) {
            printer.printOptions(
                "Register",
                "Login",
                "Exit"
            )

            val input = reader.readInt()
            when (input) {
                1 -> {
                    goToRegisterScreen()
                }

                2 -> {
                    goToLoginScreen()
                }
                3 -> {
                    printer.printGoodbyeMessage("See you later!")
                    break
                }

                else -> {
                    printer.printError("Invalid option")
                }
            }
        }
    }

    private suspend fun goToRegisterScreen() {
        registerUi.show()
    }

    private suspend fun goToLoginScreen() {
        loginUi.show()
    }

}