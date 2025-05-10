package domain.exception.handler

import core.exception.AuthenticationException
import data.constants.StringConstants.General.UNEXPECTED_ERROR
import domain.constants.StringConstants.Authentication.USER_NOT_FOUND
import domain.constants.StringConstants.General.EMPTY_INPUT
import domain.constants.StringConstants.General.NULL_INPUT
import domain.constants.StringConstants.Project.EMPTY_PROJECT_DESCRIPTION
import domain.constants.StringConstants.Project.EMPTY_PROJECT_TITLE
import domain.constants.StringConstants.Project.NULL_PROJECT_COMPARISON
import domain.constants.StringConstants.State.EMPTY_STATE_NAME
import domain.constants.StringConstants.Task.EMPTY_TASK_DESCRIPTION
import domain.constants.StringConstants.Task.EMPTY_TASK_TITLE
import domain.constants.StringConstants.Task.NULL_TASK_COMPARISON
import domain.exception.*
import org.example.ui.common.components.Printer

class DomainExceptionHandler(
    private val printer: Printer,
) : ExceptionHandler {
    override suspend fun handle(exception: Throwable) {
        val errorMessage = when (exception) {
            is GeneralException -> handleGeneralException(exception)
            is AuthenticationException -> handleAuthenticationException(exception)
            else -> handleUnexpectedException(exception)
        }
        printer.printError(errorMessage)
    }

    private fun handleGeneralException(exception: Throwable): String {
        return when (exception) {
            is NullInputException -> handleNullInputException(exception)
            else -> handleUnexpectedException(exception)
        }
    }

    private fun handleAuthenticationException(exception: Throwable): String {
        return when (exception) {
            is UserNotFoundException -> USER_NOT_FOUND
            else -> handleUnexpectedException(exception)
        }
    }

    private fun handleNullInputException(exception: Throwable): String {
        return when (exception) {
            is NullProjectsComparisonException -> NULL_PROJECT_COMPARISON
            is NullTasksComparisonException -> NULL_TASK_COMPARISON
            is EmptyFieldException -> handleEmptyFieldException(exception)
            else -> NULL_INPUT
        }
    }

    private fun handleEmptyFieldException(exception: Throwable): String {
        return when (exception) {
            is EmptyProjectDescriptionException -> EMPTY_PROJECT_TITLE
            is EmptyProjectNameException -> EMPTY_PROJECT_DESCRIPTION
            is EmptyTaskTitleException -> EMPTY_TASK_TITLE
            is EmptyTaskDescriptionException -> EMPTY_TASK_DESCRIPTION
            is EmptyStateNameException -> EMPTY_STATE_NAME
            else -> EMPTY_INPUT
        }
    }

    private fun handleUnexpectedException(exception: Throwable): String {
        return "$UNEXPECTED_ERROR: ${exception.message}"
    }
}