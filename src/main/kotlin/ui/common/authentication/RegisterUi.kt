package org.example.ui.common.authentication

import domain.exception.EmptyFieldException
import domain.exception.handler.ExceptionHandler
import domain.use_case.authentication.RegisterUserUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen

class RegisterUi(
    private val reader: Reader,
    private val printer: Printer,
    private val registerUseCase: RegisterUserUseCase,
    private val loginUi: LoginUi,
    private val exceptionHandler: ExceptionHandler,
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Register for Plan Mate")

        printer.printInfoLine("Please enter your details to register:")

        takeUserRegisterInput()
    }

    private suspend fun takeUserRegisterInput() {
        exceptionHandler.tryCatchingAsync(
            action = {
                val name = takeUserInput("Name")
                val email = takeUserInput("Email")
                val password = takeUserInput("Password")

                registerUseCase.add(name = name, email = email, password = password)

                printer.printCorrectOutput("Register successfully!")
                goToLoginScreen()
            },
            onError = {
                printer.printError("Register failed!")
                takeUserRegisterInput()
            }
        )
    }

    private fun takeUserInput(prompt: String): String {
        printer.printInfoLine("$prompt: ")
        return reader.readInput() ?: throw EmptyFieldException()
    }

    private suspend fun goToLoginScreen() {
        loginUi.show()
    }
}