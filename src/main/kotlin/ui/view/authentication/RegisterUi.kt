package ui.view.authentication

import domain.exception.EmptyFieldException
import domain.exception.handler.SafeExecutor
import domain.use_case.authentication.RegisterUserUseCase
import domain.exception.handler.ExceptionHandler
import ui.components.Printer
import ui.components.Reader
import ui.components.UiScreen

class RegisterUi(
    private val reader: Reader,
    private val printer: Printer,
    private val registerUseCase: RegisterUserUseCase,
    private val loginUi: LoginUi,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Register for Plan Mate")

        printer.printInfoLine("Please enter your details to register:")

        takeUserRegisterInput()
    }

    private suspend fun takeUserRegisterInput() {
        val name = takeUserInput("Name")
        val email = takeUserInput("Email")
        val password = takeUserInput("Password")
        executor.tryToExecute(
            action = {
                registerUseCase.registerUser(name = name, email = email, password = password)
            },
            onSuccess = {
                printer.printCorrectOutput("Register successfully!")
                goToLoginScreen()
            },
            onError = {
                handler.printHandledError(it)
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