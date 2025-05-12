package domain.exception.handler

import domain.constants.StringConstants.Authentication.EMAIL_ALREADY_EXISTS
import domain.constants.StringConstants.Authentication.EMAIL_NOT_FOUND
import domain.constants.StringConstants.Authentication.INVALID_CREDENTIALS
import domain.constants.StringConstants.Authentication.INVALID_EMAIL_FORMAT
import domain.constants.StringConstants.Authentication.UNKNOWN_AUTH_ERROR
import domain.constants.StringConstants.Authentication.USERS_ALREADY_EXIST
import domain.constants.StringConstants.Authentication.USER_NOT_LOGGED_IN
import domain.constants.StringConstants.File.EMPTY_CSV_FILE
import domain.constants.StringConstants.File.INVALID_DATA_FILE
import domain.constants.StringConstants.File.INVALID_FILE_NAME
import domain.constants.StringConstants.File.MISSING_ENTITY
import domain.constants.StringConstants.File.UNKNOWN_FILE_ERROR
import domain.constants.StringConstants.General.UNEXPECTED_ERROR
import domain.constants.StringConstants.General.UNKNOWN_ERROR
import domain.constants.StringConstants.Logs.NO_PROJECT_LOGS_AVAILABLE
import domain.constants.StringConstants.Logs.NO_TASK_LOGS_AVAILABLE
import domain.constants.StringConstants.Logs.UNKNOWN_LOG_ERROR
import domain.constants.StringConstants.Project.NO_PROJECT_FOUND
import domain.constants.StringConstants.Project.PROJECT_CREATION_FAILED
import domain.constants.StringConstants.Project.PROJECT_DELETION_FAILED
import domain.constants.StringConstants.Project.PROJECT_EDIT_FAILED
import domain.constants.StringConstants.Project.PROJECT_FETCH_ALL_FAILED
import domain.constants.StringConstants.Project.DUPLICATE_STATE
import domain.constants.StringConstants.Project.NO_STATE_FOUND
import domain.constants.StringConstants.Project.UNKNOWN_PROJECT_ERROR
import domain.constants.StringConstants.Task.FAILED_TO_READ_TASK
import domain.constants.StringConstants.Task.TASK_CREATION_FAILED
import domain.constants.StringConstants.Task.TASK_DELETION_FAILED
import domain.constants.StringConstants.Task.TASK_EDIT_FAILED
import domain.constants.StringConstants.Task.TASK_FETCH_ALL_FAILED
import domain.constants.StringConstants.Task.TASK_NOT_FOUND
import domain.constants.StringConstants.Task.UNKNOWN_TASK_ERROR
import domain.exception.*
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
import ui.components.Printer

class ExceptionHandler(
    private val printer: Printer
) {

    fun printHandledError(exception: Throwable) {
        printer.printError(mapExceptionToMessage(exception))
    }

    fun mapExceptionToMessage(exception: Throwable): String {
        return when (exception) {
            is GeneralException -> handleGeneralException(exception)
            is FileException -> handleFileException(exception)
            is AuthenticationException -> handleAuthenticationException(exception)
            is ProjectException -> handleProjectException(exception)
            is TaskException -> handleTaskException(exception)
            is LogException -> handleLogException(exception)
            else -> handleUnexpectedException(exception = exception)
        }
    }

    private fun handleGeneralException(exception: Throwable): String {
        return when (exception) {
            is NullInputException -> handleNullInputException(exception)
            is EmptyFieldException -> handleEmptyFieldException(exception)
            else -> handleUnexpectedException(exception = exception)
        }
    }

    private fun handleNullInputException(exception: Throwable): String {
        return when (exception) {
            is NullProjectsComparisonException -> NULL_PROJECT_COMPARISON
            is NullTasksComparisonException -> NULL_TASK_COMPARISON
            else -> NULL_INPUT
        }
    }

    private fun handleEmptyFieldException(exception: Throwable): String {
        return when (exception) {
            is EmptyProjectTitleException -> EMPTY_PROJECT_TITLE
            is EmptyProjectDescriptionException -> EMPTY_PROJECT_DESCRIPTION
            is EmptyTaskTitleException -> EMPTY_TASK_TITLE
            is EmptyTaskDescriptionException -> EMPTY_TASK_DESCRIPTION
            is EmptyStateNameException -> EMPTY_STATE_NAME
            else -> EMPTY_INPUT
        }
    }

    private fun handleFileException(exception: Throwable): String {
        return when (exception) {
            is InvalidFileNameException -> INVALID_FILE_NAME
            is InvalidDataFileException -> INVALID_DATA_FILE
            is EmptyCSVFileException -> EMPTY_CSV_FILE
            is CsvValidationException -> MISSING_ENTITY
            else -> handleUnexpectedException(UNKNOWN_FILE_ERROR, exception)
        }
    }

    private fun handleAuthenticationException(exception: Throwable): String {
        return when (exception) {
            is EmailAlreadyExistsException -> EMAIL_ALREADY_EXISTS
            is InvalidEmailFormatException -> INVALID_EMAIL_FORMAT
            is EmailNotFoundException -> EMAIL_NOT_FOUND
            is InvalidCredentialsException -> INVALID_CREDENTIALS
            is UsersAlreadyExistException -> USERS_ALREADY_EXIST
            is UserNotLoggedInException -> USER_NOT_LOGGED_IN
            is UserNotFoundException -> USER_NOT_FOUND
            else -> handleUnexpectedException(UNKNOWN_AUTH_ERROR, exception)
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
            else -> handleUnexpectedException(UNKNOWN_PROJECT_ERROR, exception)
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
            else -> handleUnexpectedException(UNKNOWN_TASK_ERROR, exception)
        }
    }

    private fun handleLogException(exception: Throwable): String {
        return when (exception) {
            is NoProjectLogsFoundException -> NO_PROJECT_LOGS_AVAILABLE
            is NoTaskLogsFoundException -> NO_TASK_LOGS_AVAILABLE
            else -> handleUnexpectedException(UNKNOWN_LOG_ERROR, exception)
        }
    }

    private fun handleUnexpectedException(exceptionType: String = UNEXPECTED_ERROR, exception: Throwable): String {
        return "${exceptionType}: ${exception.message ?: UNKNOWN_ERROR}"
    }
}