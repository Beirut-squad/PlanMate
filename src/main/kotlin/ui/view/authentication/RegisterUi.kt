package ui.view.authentication

import ui.common.exception.EmptyFieldException
import ui.common.exception.handler.SafeExecutor
import domain.use_case.authentication.RegisterUserUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen

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

        printer.printInfoLine("Please enter your registration credentials :")

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