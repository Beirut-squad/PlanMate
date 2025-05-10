package org.example.ui.exception

import domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer

class UIExceptionHandler(
    private val printer: Printer,
) : ExceptionHandler {
    override suspend fun handle(exception: Throwable) {
        printer.printError(exception.message ?: "Unexpected error")
    }
}