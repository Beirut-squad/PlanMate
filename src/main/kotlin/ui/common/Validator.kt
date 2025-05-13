package ui.common

import ui.common.exception.EmptyFieldException
import ui.common.exception.InvalidEmailFormatException
import ui.common.exception.ShortNameException
import ui.common.exception.WeekPasswordException

class Validator {

    fun checkEmail(email: String) {
        val emailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
        if (email.isBlank()) {
            throw EmptyFieldException()
        } else if (!emailRegex.matches(email)) {
            throw InvalidEmailFormatException()
        }
    }

    fun checkPassword(password: String) {
        if (password.isBlank()) {
            throw EmptyFieldException()
        }
        else if (password.length < 8) {
            throw WeekPasswordException()
        }
    }

    fun checkName(name: String) {
        if (name.isBlank()) {
            throw EmptyFieldException()
        }
        else if (name.length < 2) {
            throw ShortNameException()
        }
    }
}