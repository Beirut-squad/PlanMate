package ui.view.authentication

import ui.common.Reader
import ui.common.UiScreen
import ui.common.Printer

class StartUpMenuUi(
    private val reader: Reader,
    private val printer: Printer,
    private val registerUi: RegisterUi,
    private val loginUi: LoginUi
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Welcome to Plan Mate\nwhat would you like to do?")

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