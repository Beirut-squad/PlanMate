package org.example.ui.utils

import org.example.ui.Reader
import ui.Viewer

interface InputHandler {
    fun takeInput(viewer: Viewer, reader: Reader, prompt: String = "", maxRetries: Int = 3): String
    fun takeInputInt(viewer: Viewer, reader: Reader, prompt: String = "", maxRetries: Int = 3): Int
}