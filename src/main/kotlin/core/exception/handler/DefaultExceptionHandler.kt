package core.exception.handler

import data.constants.StringConstants.General.UNEXPECTED_ERROR
import domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer

class DefaultExceptionHandler(
    private val printer: Printer,
) : ExceptionHandler {

    override suspend fun handle(exception: Throwable) {
        val errorMessage = when (exception) {
            else -> handleUnexpectedException(exception)
        }
        printer.printError(errorMessage)
    }

    private fun handleUnexpectedException(exception: Throwable): String {
        return "$UNEXPECTED_ERROR: ${exception.message}"
    }
}

