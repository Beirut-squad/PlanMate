package org.example.logic.exceptions

class ErrorHandler {
    fun handle(error: Throwable) {
        when (error) {
//            is ExampleException -> println("Custom error: ${error.message}")
            else -> println("Unexpected error: ${error.message}")
        }
    }
}