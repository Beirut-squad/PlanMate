package org.example.ui.common.components

class Viewer(
    private val colors: Colors
) {

    fun printTitle(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.cyan(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printError(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.red(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printCorrectOutput(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.green(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printLoader(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.blue(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printOption(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.purple(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printExitOption(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.red(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printWelcomeMessage(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.cyan(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printInfoLine(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.yellow(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printPlainText(
        text: String,
        withNewLine: Boolean = true
    ) {
        if (withNewLine) {
            println(text)
        } else {
            print(text)
        }
    }

    fun printGoodbyeMessage(
        text: String,
        withNewLine: Boolean = true
    ) {
        val coloredText = colors.yellow(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }

    fun printOptions(vararg options: String) {
        for (i in options.indices) {
            printOption("${i + 1}. ${options[i]}")
        }
    }

    fun printOptions(options: List<String>) {
        for (i in options.indices) {
            printOption("${i + 1}. ${options[i]}")
        }
    }
    fun readIntInput(prompt: String): Int? {
        printPlainText(prompt, withNewLine = false)
        val input = readLine()
        return input?.toIntOrNull()
    }

}