package org.example.ui.common.authentication

import domain.use_case.authentication.RegisterUserUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen

class RegisterUi(
    private val reader: Reader,
    private val printer: Printer,
    private val registerUseCase: RegisterUserUseCase,
    private val loginUi: LoginUi,

    ) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Register for Plan Mate")

        printer.printInfoLine("Please enter your details to register:")

        takeUserRegisterInput()
    }

    private suspend fun takeUserRegisterInput() {
        try {
            val name = takeUserInput("Name")
            val email = takeUserInput("Email")
            val password = takeUserInput("Password")

            registerUseCase.add(name = name, email = email, password = password)

            printer.printCorrectOutput("Register successfully!")
            goToLoginScreen()
        }catch (e: Exception) {
            printer.printError("Register failed!")
            takeUserRegisterInput()
        }
    }

    private fun takeUserInput(prompt: String): String {
        printer.printInfoLine("$prompt: ")
        try {
            return reader.readInput() ?: throw Exception()
        } catch (e: Exception) {
            printer.printError("Invalid input")
            return takeUserInput(prompt)
        }
    }

    private suspend fun goToLoginScreen() {
        loginUi.show()
    }
}