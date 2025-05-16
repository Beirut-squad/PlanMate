package ui.view.authentication

import ui.common.exception.EmptyFieldException
import ui.common.exception.handler.ExceptionHandler
import ui.common.exception.handler.SafeExecutor
import domain.model.UserRole
import domain.model.User
import domain.useCase.authentication.LoginUseCase
import ui.view.user.mate.MateUi
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import ui.common.Validator
import ui.view.user.admin.home.AdminUi

class LoginUi(
    private val reader: Reader,
    private val printer: Printer,
    private val loginUseCase: LoginUseCase,
    private val adminUi: AdminUi,
    private val mateUI: MateUi,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
    private val validator: Validator
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Login for Plan Mate")

        printer.printInfoLine("Please enter your information to login:")

        takeUserLoginInput()
    }

    private suspend fun takeUserLoginInput() {
        executor.tryToExecute(
            action = {
                val email = takeUserInput("Email")
                validator.checkEmail(email)

                val password = takeUserInput("Password")
                validator.checkPassword(password)

                loginUseCase.login(email, password)
            },
            onError = {
                handler.printHandledError(it)
            },
            onSuccess = {
                printer.printCorrectOutput("Welcome ${it.name}")
                printer.printCorrectOutput("Login successful!")
                checkAdminOrMate(it)
            }
        )

    }

    private fun takeUserInput(prompt: String): String {
        printer.printInfoLine("$prompt: ")
        return reader.readInput() ?: throw EmptyFieldException()
    }

    private suspend fun checkAdminOrMate(user: User) {
        if (user.userRole == UserRole.ADMIN) {
            goToAminHomeScreen()
        } else {
            goToMateHomeScreen()
        }
    }

    private suspend fun goToAminHomeScreen() {
        adminUi.show()
    }

    private suspend fun goToMateHomeScreen() {
        mateUI.show()
    }
}