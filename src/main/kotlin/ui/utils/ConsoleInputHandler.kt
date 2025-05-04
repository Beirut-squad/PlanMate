package org.example.ui.utils

import org.example.logic.exceptions.ErrorHandler
import org.example.logic.exceptions.MaxRetriesExceededException
import org.example.logic.exceptions.NullInputException
import org.example.ui.Reader
import ui.Viewer

object ConsoleInputHandler : InputHandler {
    override fun takeInput(viewer: Viewer, reader: Reader, prompt: String, maxRetries: Int): String {
        return takeGenericInput(viewer, reader, prompt, maxRetries) {
            readInput()?.takeIf { it.isNotBlank() }
        }
    }

    override fun takeInputInt(viewer: Viewer, reader: Reader, prompt: String, maxRetries: Int): Int {
        return takeGenericInput(viewer, reader, prompt, maxRetries) {
            readInt()
        }
    }

    private fun <T> takeGenericInput(
        viewer: Viewer, reader: Reader, prompt: String = "", remainingRetries: Int = 3, readAction: Reader.() -> T?
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