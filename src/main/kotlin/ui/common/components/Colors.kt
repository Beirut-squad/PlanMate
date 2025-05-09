package org.example.ui.common.components

class Colors {

    companion object {
        const val RED = "\u001B[31m"
        const val GREEN = "\u001B[32m"
        const val YELLOW = "\u001B[33m"
        const val BLUE = "\u001B[34m"
        const val PURPLE = "\u001B[35m"
        const val CYAN = "\u001B[36m"
        const val RESET = "\u001B[0m"
    }

    fun red(text: String): String = "$RED$text$RESET"
    fun green(text: String): String = "$GREEN$text$RESET"
    fun yellow(text: String): String = "$YELLOW$text$RESET"
    fun blue(text: String): String = "$BLUE$text$RESET"
    fun purple(text: String): String = "$PURPLE$text$RESET"
    fun cyan(text: String): String = "$CYAN$text$RESET"


}