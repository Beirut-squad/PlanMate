package org.example.ui.authentication_screens

import org.example.constants.StringConstants
import org.example.logic.exceptions.ErrorHandler
import org.example.logic.use_cases.authentication.RegisterUserOrAdminUseCase
import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.utils.InputUtils
import ui.Viewer

class RegisterScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val registerUseCase: RegisterUserOrAdminUseCase,
    private val loginScreen: LoginScreen,
    ) : UiScreen {
    override fun show() {
        viewer.printTitle(StringConstants.RegisterScreen.WELCOME_REGISTER)
        viewer.printInfoLine(StringConstants.RegisterScreen.REGISTRATION_DETAILS)
        takeUserRegisterInput()
    }

    private fun takeUserRegisterInput() {
        val name = InputUtils.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME)
        val email = InputUtils.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
        val password = InputUtils.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)

        registerUseCase.add(name = name, email = email, password = password)
            .onSuccess {
                viewer.printInfoLine(StringConstants.RegisterScreen.REGISTER_SUCCESS)
                goToLoginScreen()
            }
            .onFailure {
                ErrorHandler().handle(it)
                takeUserRegisterInput()
            }
    }

    private fun goToLoginScreen() {
        loginScreen.show()
    }
}