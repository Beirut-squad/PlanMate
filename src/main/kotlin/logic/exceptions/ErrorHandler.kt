package org.example.logic.exceptions

import org.example.constants.StringConstants.Project.NO_PROJECT_FOUND

class ErrorHandler {
    fun handle(error: Throwable) {
        when (error) {
            is NoProjectFoundException -> println(error.message)
            is DuplicateStateException -> println(error.message)
            is NoStateException -> println(error.message)
            is StateHasAssociatedTasksException -> println(error.message)
            else -> println("Unexpected error: ${error.message}")
        }
    }
}