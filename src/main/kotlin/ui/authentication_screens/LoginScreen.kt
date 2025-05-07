package org.example.ui.authentication_screens

import org.example.logic.use_cases.authentication.LoginUseCase
import org.example.models.Role
import org.example.models.User
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.admin.home_screen.AdminHomeScreen
import org.example.ui.mate.home_screen.MateHomeUI
import org.example.ui.common.components.Viewer

class LoginScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val loginUseCase: LoginUseCase,
    private val adminHomeScreen: AdminHomeScreen,
    private val mateHomeUI: MateHomeUI,
) : UiScreen {
    override fun show() {
        viewer.printTitle("Login for Plan Mate")

        viewer.printInfoLine("Please enter your information to login:")

        takeUserLoginInput()
    }

    private fun takeUserLoginInput() {
        val email = takeUserInput("Email")
        val password = takeUserInput("Password")

        loginUseCase.login(email, password)
            .onSuccess { user ->
                viewer.printInfoLine("Login successful!")
                checkAdminOrMate(user)
            }
            .onFailure {
                viewer.printError("Login failed!")
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

    private fun checkAdminOrMate(user: User) {
        if (user.role == Role.ADMIN) {
            goToAminHomeScreen()
        } else {
            goToMateHomeScreen()
        }
    }

    private fun goToAminHomeScreen() {
        adminHomeScreen.show()
    }

    private fun goToMateHomeScreen() {
        mateHomeUI.show()
    }
}