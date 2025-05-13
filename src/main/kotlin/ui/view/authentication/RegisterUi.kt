package ui.view.authentication

import ui.common.exception.EmptyFieldException
import ui.common.exception.handler.SafeExecutor
import domain.use_case.authentication.RegisterUserUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import ui.common.Validator

class RegisterUi(
    private val reader: Reader,
    private val printer: Printer,
    private val registerUseCase: RegisterUserUseCase,
    private val loginUi: LoginUi,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
    private val validator: Validator
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Register for Plan Mate")

        printer.printInfoLine("Please enter your registration credentials :")

        takeUserRegisterInput()
    }

    private suspend fun takeUserRegisterInput() {
        executor.tryToExecute(
            action = {
                val name = takeUserInput("Name")
                validator.checkName(name)

                val email = takeUserInput("Email")
                validator.checkEmail(email)

                val password = takeUserInput("Password")
                validator.checkPassword(password)

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