package org.example.ui.authentication_screens

import org.example.logic.use_case.authentication.RegisterUserOrAdminUseCase
import org.example.models.User
import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.home_screens.HomeScreen
import ui.Viewer

class RegisterScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val registerUseCase: RegisterUserOrAdminUseCase,
    private val homeScreen: HomeScreen

) : UiScreen {
    override fun show() {
        viewer.printTitle("Register for Plan Mate")

        viewer.printInfoLine("Please enter your details to register:")

        takeUserRegisterInput()
    }

    private fun takeUserRegisterInput() {
        val name = takeUserInput("Name")
        val email = takeUserInput("Email")
        val password = takeUserInput("Password")

        registerUseCase.add(name = name, email = email, password = password)
            .onSuccess { user ->
                viewer.printInfoLine("Register successfully!")
                goToHomeScreen(user)
            }
            .onFailure {
                viewer.printError("Register failed!")
                takeUserRegisterInput()
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