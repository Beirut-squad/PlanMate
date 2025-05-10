package data.exception.handler

import core.exception.AuthenticationException
import data.constants.StringConstants.Authentication.EMAIL_ALREADY_EXISTS
import data.constants.StringConstants.Authentication.EMAIL_NOT_FOUND
import data.constants.StringConstants.Authentication.INVALID_CREDENTIALS
import data.constants.StringConstants.Authentication.INVALID_EMAIL_FORMAT
import data.constants.StringConstants.Authentication.UNKNOWN_AUTH_ERROR
import data.constants.StringConstants.Authentication.USERS_ALREADY_EXIST
import data.constants.StringConstants.Authentication.USER_NOT_LOGGED_IN
import data.constants.StringConstants.General.UNEXPECTED_ERROR
import data.constants.StringConstants.General.UNKNOWN_ERROR
import data.constants.StringConstants.Logs.NO_PROJECT_LOGS_AVAILABLE
import data.constants.StringConstants.Logs.NO_TASK_LOGS_AVAILABLE
import data.constants.StringConstants.Logs.UNKNOWN_LOG_ERROR
import data.constants.StringConstants.Project.NO_PROJECT_FOUND
import data.constants.StringConstants.Project.PROJECT_CREATION_FAILED
import data.constants.StringConstants.Project.PROJECT_DELETION_FAILED
import data.constants.StringConstants.Project.PROJECT_EDIT_FAILED
import data.constants.StringConstants.Project.PROJECT_FETCH_ALL_FAILED
import data.constants.StringConstants.Project.DUPLICATE_STATE
import data.constants.StringConstants.Project.NO_STATE_FOUND
import data.constants.StringConstants.Project.UNKNOWN_PROJECT_ERROR
import data.constants.StringConstants.Task.FAILED_TO_READ_TASK
import data.constants.StringConstants.Task.TASK_CREATION_FAILED
import data.constants.StringConstants.Task.TASK_DELETION_FAILED
import data.constants.StringConstants.Task.TASK_EDIT_FAILED
import data.constants.StringConstants.Task.TASK_FETCH_ALL_FAILED
import data.constants.StringConstants.Task.TASK_NOT_FOUND
import data.constants.StringConstants.Task.UNKNOWN_TASK_ERROR
import data.exception.*
import domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer

class DataExceptionHandler(
    private val printer: Printer,
) : ExceptionHandler {
    override suspend fun handle(exception: Throwable) {
        val errorMessage = when (exception) {
            is AuthenticationException -> handleAuthenticationException(exception)
            is ProjectException -> handleProjectException(exception)
            is TaskException -> handleTaskException(exception)
            is LogException -> handleLogException(exception)
            else -> handleUnexpectedException(exception)
        }
        printer.printError(errorMessage)
    }

    private fun handleAuthenticationException(exception: Throwable): String {
        return when (exception) {
            is EmailAlreadyExistsException -> EMAIL_ALREADY_EXISTS
            is InvalidEmailFormatException -> INVALID_EMAIL_FORMAT
            is EmailNotFoundException -> EMAIL_NOT_FOUND
            is InvalidCredentialsException -> INVALID_CREDENTIALS
            is UsersAlreadyExistException -> USERS_ALREADY_EXIST
            is UserNotLoggedInException -> USER_NOT_LOGGED_IN
            else -> "${UNKNOWN_AUTH_ERROR}: ${exception.message ?: UNKNOWN_ERROR}"
        }
    }

    private fun handleProjectException(exception: Throwable): String {
        return when (exception) {
            is ProjectNotFoundException -> NO_PROJECT_FOUND
            is ProjectCreationFailedException -> PROJECT_CREATION_FAILED
            is ProjectEditFailedException -> PROJECT_EDIT_FAILED
            is ProjectDeletionFailedException -> PROJECT_DELETION_FAILED
            is ProjectFetchAllFailedException -> PROJECT_FETCH_ALL_FAILED
            is DuplicateStateException -> DUPLICATE_STATE
            is StateNotFoundException -> NO_STATE_FOUND
            else -> "${UNKNOWN_PROJECT_ERROR}: ${exception.message ?: UNKNOWN_ERROR}"
        }
    }

    private fun handleTaskException(exception: Throwable): String {
        return when (exception) {
            is TaskCreationFailedException -> TASK_CREATION_FAILED
            is TaskEditFailedException -> TASK_EDIT_FAILED
            is TaskDeletionFailedException -> TASK_DELETION_FAILED
            is TaskFetchAllFailedException -> TASK_FETCH_ALL_FAILED
            is TaskNotFoundException -> TASK_NOT_FOUND
            is FailedToReadTaskException -> FAILED_TO_READ_TASK
            else -> "${UNKNOWN_TASK_ERROR}: ${exception.message ?: UNKNOWN_ERROR}"
        }
    }

    private fun handleLogException(exception: Throwable): String {
        return when (exception) {
            is NoProjectLogsFoundException -> NO_PROJECT_LOGS_AVAILABLE
            is NoTaskLogsFoundException -> NO_TASK_LOGS_AVAILABLE
            else -> "${UNKNOWN_LOG_ERROR}: ${exception.message ?: UNKNOWN_ERROR}"
        }
    }

    private fun handleUnexpectedException(exception: Throwable): String {
        return "${UNEXPECTED_ERROR}: ${exception.message}"
    }
}