package org.example.constants

object StringConstants {
    // region General
    object General {
        const val INVALID_INPUT = "Invalid option"
    }
    // endregion

    // Project-related
    object Project {
        const val NO_PROJECT_FOUND = "No project found. Please make sure the project exists before proceeding."
        const val ADDED_STATE_SUCCESS = "State added successfully."
        const val UPDATED_STATE_SUCCESS = "State updated successfully."
        const val REMOVED_STATE_SUCCESS = "State deleted successfully."
        const val DUPLICATE_STATE = "You cannot add this state because it already exists."
        const val NO_STATE_FOUND = "No state found. Please make sure the state exists before proceeding."
        const val STATE_HAS_TASKS = "Cannot remove state stateName because it has associated tasks."
    }

//    // Validation
//    object Validation {
//        const val INVALID_EMAIL = "Invalid email format"
//    }
}