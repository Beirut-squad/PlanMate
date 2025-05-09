package org.example.ui.common.authentication

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
) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Login for Plan Mate")

        printer.printInfoLine("Please enter your information to login:")

        takeUserLoginInput()
    }

    private suspend fun takeUserLoginInput() {
        val email = takeUserInput("Email")
        val password = takeUserInput("Password")

        try {
            val user = loginUseCase.login(email, password)
            printer.printCorrectOutput("Login successful!")
            checkAdminOrMate(user)
            return
        } catch (e: Exception) {
            printer.printError("Login failed! ${e.message}")
            takeUserLoginInput()
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