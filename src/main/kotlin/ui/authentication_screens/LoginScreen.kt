package org.example.ui.authentication_screens

import org.example.logic.use_cases.authentication.LoginUseCase
import org.example.models.User
import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.home_screens.HomeScreen
import ui.Viewer

class LoginScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val loginUseCase: LoginUseCase,
    private val homeScreen: HomeScreen
): UiScreen {
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
                goToHomeScreen(user)
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

    private fun goToHomeScreen(user: User) {
        homeScreen.show()
    }
}