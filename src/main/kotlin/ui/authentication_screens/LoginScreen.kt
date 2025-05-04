package org.example.ui.authentication_screens

import org.example.constants.StringConstants
import org.example.logic.exceptions.ErrorHandler
import org.example.logic.use_cases.authentication.LoginUseCase
import org.example.models.Role
import org.example.models.User
import org.example.ui.Reader
import org.example.ui.UiScreen
import org.example.ui.home_screens.admin.ui.home_screens.admin.AdminHomeScreen
import org.example.ui.home_screens.mate.ui.home_screens.mate.MateHomeScreen
import org.example.ui.utils.ConsoleInputHandler
import org.example.ui.utils.InputHandler
import ui.Viewer

class LoginScreen(
    private val reader: Reader,
    private val viewer: Viewer,
    private val loginUseCase: LoginUseCase,
    private val adminHomeScreen: AdminHomeScreen,
    private val mateHomeScreen: MateHomeScreen,
    private val inputHandler: InputHandler,
    private val errorHandler: ErrorHandler,
) : UiScreen {
    override fun show() {
        viewer.printTitle(StringConstants.LoginScreen.WELCOME_LOGIN)
        viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_DETAILS)
        takeUserLoginInput()
    }

    private fun takeUserLoginInput() {
        val email = inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
        val password = inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)
        loginUseCase.login(email, password).onSuccess { user ->
            viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_SUCCESS)
            checkAdminOrMate(user)
        }.onFailure {
            errorHandler.handle(it)
            takeUserLoginInput()
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
        mateHomeScreen.show()
    }
}