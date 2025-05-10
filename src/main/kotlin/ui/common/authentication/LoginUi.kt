package org.example.ui.common.authentication

import domain.exception.EmptyFieldException
import domain.exception.handler.ExceptionHandler
import domain.model.Role
import domain.model.User
import domain.use_case.authentication.LoginUseCase
import org.example.ui.admin.home.AdminUi
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.mate.MateUi

class LoginUi(
    private val reader: Reader,
    private val printer: Printer,
    private val loginUseCase: LoginUseCase,
    private val adminUi: AdminUi,
    private val mateUI: MateUi,
    private val exceptionHandler: ExceptionHandler,
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Login for Plan Mate")

        printer.printInfoLine("Please enter your information to login:")

        takeUserLoginInput()
    }

    private suspend fun takeUserLoginInput() {
        exceptionHandler.runSafely {
            val email = takeUserInput("Email")
            val password = takeUserInput("Password")

            loginUseCase.login(email, password)
        }.fold(
            onFailure = {
                takeUserLoginInput()
            },
            onSuccess = {
                printer.printCorrectOutput("Login successful!")
                checkAdminOrMate(it)
                return
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