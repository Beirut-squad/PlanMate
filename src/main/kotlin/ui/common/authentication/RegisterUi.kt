package org.example.ui.common.authentication

import org.example.logic.use_cases.authentication.RegisterUserOrAdminUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class RegisterUi(
    private val reader: Reader,
    private val viewer: Viewer,
    private val registerUseCase: RegisterUserOrAdminUseCase,
    private val loginUi: LoginUi,

    ) : UiScreen {
    override suspend fun show() {
        viewer.printTitle("Register for Plan Mate")

        viewer.printInfoLine("Please enter your details to register:")

        takeUserRegisterInput()
    }

    private suspend fun takeUserRegisterInput() {
        try {
            val name = takeUserInput("Name")
            val email = takeUserInput("Email")
            val password = takeUserInput("Password")

            registerUseCase.add(name = name, email = email, password = password)

            viewer.printCorrectOutput("Register successfully!")
            goToLoginScreen()
        }catch (e: Exception) {
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

    private suspend fun goToLoginScreen() {
        loginUi.show()
    }
}