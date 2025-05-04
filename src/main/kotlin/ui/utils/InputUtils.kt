package org.example.ui.utils

import org.example.constants.StringConstants
import org.example.logic.exceptions.ErrorHandler
import org.example.logic.exceptions.MaxRetriesExceededException
import org.example.logic.exceptions.NullInputException
import org.example.ui.Reader
import ui.Viewer

object InputUtils {
    fun takeInput(viewer: Viewer, reader: Reader, prompt: String, maxRetries: Int = 3): String {
        return takeGenericInput(viewer, reader, prompt, maxRetries) {
            readInput()?.takeIf { it.isNotBlank() }
        }
    }

    fun takeInputInt(viewer: Viewer, reader: Reader, prompt: String = "", maxRetries: Int = 3): Int {
        return takeGenericInput(viewer, reader, prompt, maxRetries) {
            readInt()
        }
    }

    private fun <T> takeGenericInput(
        viewer: Viewer,
        reader: Reader,
        prompt: String = "",
        remainingRetries: Int = 3,
        readAction: Reader.() -> T?
    ): T {
        if (prompt.isNotBlank()) viewer.printInfoLine("$prompt: ", withNewLine = false)
        return try {
            reader.readAction() ?: throw NullInputException()
        } catch (error: Exception) {
            if (remainingRetries <= 1) {
                throw MaxRetriesExceededException()
            }
            ErrorHandler().handle(error)
            takeGenericInput(viewer, reader, prompt, remainingRetries - 1, readAction)
        }
    }
}