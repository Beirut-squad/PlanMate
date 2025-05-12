package ui.view.authentication

import domain.exception.EmptyFieldException
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.model.Role
import domain.model.User
import domain.use_case.authentication.LoginUseCase
import ui.view.user.mate.MateUi
import ui.components.Printer
import ui.components.Reader
import ui.components.UiScreen
import ui.view.user.admin.home.AdminUi

class LoginUi(
    private val reader: Reader,
    private val printer: Printer,
    private val loginUseCase: LoginUseCase,
    private val adminUi: AdminUi,
    private val mateUI: MateUi,
    private val executor: SafeExecutor,
    private val handler: ExceptionHandler,
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Login for Plan Mate")

        printer.printInfoLine("Please enter your information to login:")

        takeUserLoginInput()
    }

    private suspend fun takeUserLoginInput() {
        val email = takeUserInput("Email")
        val password = takeUserInput("Password")
        executor.tryToExecute(
            action = {
                loginUseCase.login(email, password)
            },
            onError = {
                handler.printHandledError(it)
            },
            onSuccess = {
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
        if (user.role == Role.ADMIN) {
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