package ui.common.exception.handler

import ui.common.exception.constants.ErrorMessageConstants.Authentication.EMAIL_ALREADY_EXISTS
import ui.common.exception.constants.ErrorMessageConstants.Authentication.EMAIL_NOT_FOUND
import ui.common.exception.constants.ErrorMessageConstants.Authentication.INVALID_CREDENTIALS
import ui.common.exception.constants.ErrorMessageConstants.Authentication.UNKNOWN_AUTH_ERROR
import ui.common.exception.constants.ErrorMessageConstants.Authentication.USERS_ALREADY_EXIST
import ui.common.exception.constants.ErrorMessageConstants.Authentication.USER_NOT_FOUND
import ui.common.exception.constants.ErrorMessageConstants.Authentication.USER_NOT_LOGGED_IN
import ui.common.exception.constants.ErrorMessageConstants.File.EMPTY_CSV_FILE
import ui.common.exception.constants.ErrorMessageConstants.File.INVALID_DATA_FILE
import ui.common.exception.constants.ErrorMessageConstants.File.INVALID_FILE_NAME
import ui.common.exception.constants.ErrorMessageConstants.File.MISSING_ENTITY
import ui.common.exception.constants.ErrorMessageConstants.File.UNKNOWN_FILE_ERROR
import ui.common.exception.constants.ErrorMessageConstants.General.EMPTY_INPUT
import ui.common.exception.constants.ErrorMessageConstants.General.NULL_INPUT
import ui.common.exception.constants.ErrorMessageConstants.General.UNEXPECTED_ERROR
import ui.common.exception.constants.ErrorMessageConstants.General.UNKNOWN_ERROR
import ui.common.exception.constants.ErrorMessageConstants.Logs.NO_PROJECT_LOGS_AVAILABLE
import ui.common.exception.constants.ErrorMessageConstants.Logs.NO_TASK_LOGS_AVAILABLE
import ui.common.exception.constants.ErrorMessageConstants.Logs.UNKNOWN_LOG_ERROR
import ui.common.exception.constants.ErrorMessageConstants.Project.DUPLICATE_DESCRIPTION
import ui.common.exception.constants.ErrorMessageConstants.Project.DUPLICATE_STATE
import ui.common.exception.constants.ErrorMessageConstants.Project.DUPLICATE_TITLE
import ui.common.exception.constants.ErrorMessageConstants.Project.EMPTY_PROJECT_DESCRIPTION
import ui.common.exception.constants.ErrorMessageConstants.Project.EMPTY_PROJECT_TITLE
import ui.common.exception.constants.ErrorMessageConstants.Project.NO_PROJECT_FOUND
import ui.common.exception.constants.ErrorMessageConstants.Project.NO_STATE_FOUND
import ui.common.exception.constants.ErrorMessageConstants.Project.NULL_PROJECT_COMPARISON
import ui.common.exception.constants.ErrorMessageConstants.Project.PROJECT_CREATION_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Project.PROJECT_DELETION_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Project.PROJECT_EDIT_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Project.PROJECT_FETCH_ALL_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Project.UNKNOWN_PROJECT_ERROR
import ui.common.exception.constants.ErrorMessageConstants.State.EMPTY_STATE_NAME
import ui.common.exception.constants.ErrorMessageConstants.Task.EMPTY_TASK_DESCRIPTION
import ui.common.exception.constants.ErrorMessageConstants.Task.EMPTY_TASK_TITLE
import ui.common.exception.constants.ErrorMessageConstants.Task.FAILED_TO_READ_TASK
import ui.common.exception.constants.ErrorMessageConstants.Task.NULL_TASK_COMPARISON
import ui.common.exception.constants.ErrorMessageConstants.Task.TASK_CREATION_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Task.TASK_DELETION_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Task.TASK_EDIT_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Task.TASK_FETCH_ALL_FAILED
import ui.common.exception.constants.ErrorMessageConstants.Task.TASK_NOT_FOUND
import ui.common.exception.constants.ErrorMessageConstants.Task.UNKNOWN_TASK_ERROR
import ui.common.exception.constants.ErrorMessageConstants.Validator.INVALID_EMAIL_FORMAT
import ui.common.exception.constants.ErrorMessageConstants.Validator.NAME_TOO_SHORT
import ui.common.exception.constants.ErrorMessageConstants.Validator.WEAK_PASSWORD
import ui.common.Printer
import ui.common.exception.*

class ExceptionHandler(
    private val printer: Printer
) {

    fun printHandledError(exception: Throwable) {
        printer.printError(mapExceptionToMessage(exception))
    }

    private fun mapExceptionToMessage(exception: Throwable): String {
        return when (exception) {
            is GeneralException -> handleGeneralException(exception)
            is FileException -> handleFileException(exception)
            is AuthenticationException -> handleAuthenticationException(exception)
            is ProjectException -> handleProjectException(exception)
            is TaskException -> handleTaskException(exception)
            is LogException -> handleLogException(exception)
            is ValidationException -> handleValidationException(exception)
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
            is DuplicateDescriptionException -> DUPLICATE_DESCRIPTION
            is DuplicateTitleException -> DUPLICATE_TITLE
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

    private fun handleValidationException(exception: Throwable): String {
        return when (exception) {
            is InvalidEmailFormatException -> INVALID_EMAIL_FORMAT
            is ShortNameException -> NAME_TOO_SHORT
            is WeekPasswordException -> WEAK_PASSWORD
            else -> handleUnexpectedException(UNKNOWN_LOG_ERROR, exception)
        }
    }
}