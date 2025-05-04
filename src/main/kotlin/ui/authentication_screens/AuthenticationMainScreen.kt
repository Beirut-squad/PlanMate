package org.example.ui.authentication_screens

import org.example.constants.StringConstants
import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.utils.InputUtils
import ui.Viewer

class AuthenticationMainScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val registerScreen: RegisterScreen,
    private val loginScreen: LoginScreen
) : UiScreen {
    override fun show() {
        viewer.printTitle(StringConstants.AuthScreen.WELCOME)

        var running = true
        while (running) {
            viewer.printOptions(
                StringConstants.AuthScreen.REGISTER,
                StringConstants.AuthScreen.LOGIN,
            )
            viewer.printOption("0. ${StringConstants.AuthScreen.EXIT}")

            val input = InputUtils.takeInputInt(viewer,reader)
            when (input) {
                1 -> {
                    goToRegisterScreen()
                    running = false
                }

                2 -> {
                    goToLoginScreen()
                    running = false
                }

                0 -> {
                    viewer.printGoodbyeMessage("Goodbye :)")
                    break
                }

                else -> {
                    viewer.printError(StringConstants.General.INVALID_INPUT)
                }
            }
        }
    }

    private fun goToRegisterScreen() {
        registerScreen.show()
    }

    private fun goToLoginScreen() {
        loginScreen.show()
    }

}