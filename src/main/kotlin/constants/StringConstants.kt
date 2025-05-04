package org.example.constants

object StringConstants {
    // region General
    object General {
        const val INVALID_INPUT = "Invalid option"
    }
    // endregion

    // region Project-related
    object Project {
        const val NO_PROJECT_FOUND = "No project found. Please make sure the project exists before proceeding."
        const val ADDED_STATE_SUCCESS = "State added successfully."
        const val UPDATED_STATE_SUCCESS = "State updated successfully."
        const val REMOVED_STATE_SUCCESS = "State deleted successfully."
        const val DUPLICATE_STATE = "You cannot add this state because it already exists."
        const val NO_STATE_FOUND = "No state found. Please make sure the state exists before proceeding."
        const val STATE_HAS_TASKS = "Cannot remove state stateName because it has associated tasks."
    }
    // endregion

    // region AuthenticationMainScreen
    object AuthScreen {
        const val WELCOME = "Welcome to Plan Mate, what would you like to do?"
        const val REGISTER = "Register"
        const val LOGIN = "Login"
        const val EMAIL = "Email"
        const val PASSWORD = "Password"
        const val EXIT = "Exit"
        const val BYE = "Goodbye :)"
    }
    // endregion

    // region RegisterScreen
    object RegisterScreen {
        const val WELCOME_REGISTER = "Register for Plan Mate"
        const val REGISTRATION_DETAILS = "Please enter your details to register:"
        const val NAME = "Name"
        const val REGISTER_SUCCESS = "Register successfully!"
        const val REGISTER_FAILED = "Register failed!"
    }
    // endregion

    // region RegisterScreen
    object LoginScreen {
        const val WELCOME_LOGIN = "Login for Plan Mate"
        const val LOGIN_DETAILS = "Please enter your information to login:"
        const val LOGIN_SUCCESS = "Login successful!"
        const val LOGIN_FAILED = "Login failed!"
    }
    // endregion
}