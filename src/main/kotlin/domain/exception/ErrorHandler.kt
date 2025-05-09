package domain.exception

import domain.exception.project.DuplicateStateException
import domain.exception.project.NoProjectFoundException
import domain.exception.project.NoStateException
import domain.exception.project.StateHasAssociatedTasksException

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