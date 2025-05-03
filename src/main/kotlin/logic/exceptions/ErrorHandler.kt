package org.example.logic.exceptions

import org.example.logic.exceptions.project_magement_exceptions.DuplicateStateException
import org.example.logic.exceptions.project_magement_exceptions.NoProjectFoundException
import org.example.logic.exceptions.project_magement_exceptions.NoStateException
import org.example.logic.exceptions.project_magement_exceptions.StateHasAssociatedTasksException

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