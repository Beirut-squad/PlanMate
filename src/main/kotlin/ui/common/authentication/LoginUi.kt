package org.example.ui.common.authentication

import org.example.logic.use_cases.authentication.LoginUseCase
import org.example.models.Role
import org.example.models.User
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.admin.home_screen.AdminHomeUi
import org.example.ui.mate.MateHomeUi
import org.example.ui.common.components.Viewer

class LoginUi(
    private val reader: Reader,
    private val viewer: Viewer,
    private val loginUseCase: LoginUseCase,
    private val adminHomeUi: AdminHomeUi,
    private val mateHomeUI: MateHomeUi,
) : UiScreen {
    override suspend fun show() {
        viewer.printTitle("Login for Plan Mate")

        viewer.printInfoLine("Please enter your information to login:")

        takeUserLoginInput()
    }

    private suspend fun takeUserLoginInput() {
        val email = takeUserInput("Email")
        val password = takeUserInput("Password")

        try {
            val user = loginUseCase.login(email, password)
            viewer.printCorrectOutput("Login successful!")
            checkAdminOrMate(user)
            return
        } catch (e: Exception) {
            viewer.printError("Login failed! ${e.message}")
            takeUserLoginInput()
        }
    }

    private fun takeUserInput(prompt: String): String {
        viewer.printInfoLine("$prompt: ")
        try {
            return reader.readInput() ?: throw Exception()
        } catch (e: Exception) {
            viewer.printError("Invalid input")
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
        adminHomeUi.show()
    }

    private suspend fun goToMateHomeScreen() {
        mateHomeUI.show()
    }
}