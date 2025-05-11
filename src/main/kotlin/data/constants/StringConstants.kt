package data.constants

object StringConstants {

    object General {
        const val UNEXPECTED_ERROR = "Unexpected error, please try again later"
        const val UNKNOWN_ERROR = "Unknown error"
    }

    object File {
        const val INVALID_FILE_NAME = "Invalid file name. File name must be non-empty and contain only valid characters."
        const val INVALID_DATA_FILE = "Invalid data file provided"
        const val EMPTY_CSV_FILE = "Empty CSV FILE"
        const val MISSING_ENTITY = "Required entity is missing in CSV line"
        const val UNKNOWN_FILE_ERROR = "File error"
    }

    object Authentication {
        const val EMAIL_ALREADY_EXISTS = "This email is already registered."
        const val INVALID_EMAIL_FORMAT = "Invalid email format."
        const val EMAIL_NOT_FOUND = "This email is not associated with any account."
        const val INVALID_CREDENTIALS = "Incorrect email or password."
        const val USERS_ALREADY_EXIST = "Users already exist in the system."
        const val USER_NOT_LOGGED_IN = "No user is currently logged in."
        const val UNKNOWN_AUTH_ERROR = "Authentication error"
    }

    object Project {
        const val NO_PROJECT_FOUND = "No project found."
        const val DUPLICATE_STATE = "State already exists in this project."
        const val NO_STATE_FOUND = "No state found."

        const val PROJECT_CREATION_FAILED = "Failed to create project"
        const val PROJECT_EDIT_FAILED = "Failed to edit project"
        const val PROJECT_DELETION_FAILED = "Failed to delete project"
        const val PROJECT_FETCH_ALL_FAILED = "Failed to get projects"
        const val UNKNOWN_PROJECT_ERROR = "Project error"
    }

    object Task {
        const val TASK_CREATION_FAILED = "Failed to create task"
        const val TASK_EDIT_FAILED = "Failed to edit task"
        const val TASK_DELETION_FAILED = "Failed to delete task"
        const val TASK_NOT_FOUND = "No tasks found"
        const val TASK_FETCH_ALL_FAILED = "Failed to fetch tasks"
        const val FAILED_TO_READ_TASK = "Failed to read task"
        const val UNKNOWN_TASK_ERROR = "Task error"
    }

    object Logs {
        const val NO_PROJECT_LOGS_AVAILABLE = "No logs available for this project."
        const val NO_TASK_LOGS_AVAILABLE = "No logs available for this task."
        const val UNKNOWN_LOG_ERROR = "Log error"
    }
}