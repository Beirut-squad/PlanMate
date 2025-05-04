package org.example.logic.exceptions

import org.example.constants.StringConstants
import org.example.logic.exceptions.authentication_exceptions.EmailNotFoundException
import org.example.logic.exceptions.authentication_exceptions.UsersAlreadyExistException
import org.example.logic.exceptions.project_magement_exceptions.DuplicateStateException
import org.example.logic.exceptions.project_magement_exceptions.NoProjectFoundException
import org.example.logic.exceptions.project_magement_exceptions.NoStateException
import org.example.logic.exceptions.project_magement_exceptions.StateHasAssociatedTasksException
import ui.Colors
import ui.Viewer

class ErrorHandler {
    private val viewer = Viewer(Colors())

    fun handle(error: Throwable) {
        val errorMessage = error.message.toString()
        when (error) {
            is NoProjectFoundException -> viewer.printError(errorMessage)
            is DuplicateStateException -> viewer.printError(errorMessage)
            is NoStateException -> viewer.printError(errorMessage)
            is StateHasAssociatedTasksException -> viewer.printError(errorMessage)
            is MaxRetriesExceededException -> viewer.printError(errorMessage)
            is NullInputException -> viewer.printError(errorMessage)
            is EmailNotFoundException ->  viewer.printError(StringConstants.LoginScreen.LOGIN_FAILED)
            is UsersAlreadyExistException ->  viewer.printError(StringConstants.RegisterScreen.REGISTER_FAILED)
            else -> println("Unexpected error: ${error.message}")
        }
    }
}