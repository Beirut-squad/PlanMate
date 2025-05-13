package ui.components

class Validator {

    fun checkEmail(email: String): Boolean {
        val emailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
        return email.isNotBlank() && emailRegex.matches(email)
    }

    fun checkPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun checkName(name: String): Boolean {
        return name.length >= 2
    }
}