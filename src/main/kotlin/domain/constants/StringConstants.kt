package domain.constants

object StringConstants {

    object General {
        const val NULL_INPUT = "Input cannot be null."
        const val EMPTY_INPUT = "Input cannot be empty."
//        const val BLANK_FIELDS = "One or more required fields are blank."
//        const val NO_FIELDS_TO_UPDATE = "There are no fields to update."
    }

    object Authentication {
        const val USER_NOT_FOUND = "User not found with the provided credentials."
    }

    object Project {
        const val EMPTY_PROJECT_TITLE = "Project title cannot be empty."
        const val EMPTY_PROJECT_DESCRIPTION = "Project description cannot be empty."
        const val NULL_PROJECT_COMPARISON = "Both previous and current projects cannot be null."
    }

    object Task {
        const val EMPTY_TASK_TITLE = "Task title cannot be empty."
        const val EMPTY_TASK_DESCRIPTION = "Task description cannot be empty."
        const val NULL_TASK_COMPARISON = "Both previous and current tasks cannot be null."
    }

    object State {
        const val EMPTY_STATE_NAME = "State name cannot be empty."
    }
}
