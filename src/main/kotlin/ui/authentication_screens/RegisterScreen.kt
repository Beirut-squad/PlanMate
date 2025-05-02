package org.example.ui.authentication_screens

import org.example.ui.Reader
import org.example.ui.UiScreen
import ui.Viewer

class RegisterScreen(
    private val reader: Reader,
    private val viewer: Viewer

) : UiScreen {
    override fun show() {
        viewer.printTitle("Register for Plan Mate")

        while (true) {
            viewer.printInfoLine("Please enter your details to register:")

            takeUserRegisterInput()
        }
    }

    private fun takeUserRegisterInput() {
        val name = takeUserInput("Name")
        val email = takeUserInput("Email")
        val password = takeUserInput("Password")

        // TODO: Use case
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
}