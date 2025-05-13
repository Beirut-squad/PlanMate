package ui.common

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import ui.common.exception.EmptyFieldException
import ui.common.exception.InvalidEmailFormatException
import ui.common.exception.ShortNameException
import ui.common.exception.WeekPasswordException

class ValidatorTest {
    private val validator = Validator()

    // Check Email
    @Test
    fun `should not throw exception if email is valid`() {
        // Given
        val email = "email@gmail.com"

        // When & Then
        assertDoesNotThrow {
            validator.checkEmail(email)
        }
    }

    @Test
    fun `should throw exception if email is blank`() {
        // Given
        val email = ""

        // When & Then
        assertThrows<EmptyFieldException> {
            validator.checkEmail(email)
        }
    }

    @Test
    fun `should throw exception if email format is wrong`() {
        // Given
        val email = "email email email"

        // When & Then
        assertThrows<InvalidEmailFormatException> {
            validator.checkEmail(email)
        }
    }

    // Check Password
    @Test
    fun `should not throw exception if password is valid`() {
        // Given
        val password = "12345678"

        // When & Then
        assertDoesNotThrow {
            validator.checkPassword(password)
        }
    }

    @Test
    fun `should throw exception if password is blank`() {
        // Given
        val password = ""

        // When & Then
        assertThrows<EmptyFieldException> {
            validator.checkPassword(password)
        }
    }

    @Test
    fun `should throw exception if password is too weak`() {
        // Given
        val password = "12345"

        // When & Then
        assertThrows<WeekPasswordException> {
            validator.checkPassword(password)
        }
    }

    // Check Name
    @Test
    fun `should not throw exception if name is valid`() {
        // Given
        val name = "John"

        // When & Then
        assertDoesNotThrow {
            validator.checkName(name)
        }
    }

    @Test
    fun `should throw exception if name is blank`() {
        // Given
        val name = ""

        // When & Then
        assertThrows<EmptyFieldException> {
            validator.checkName(name)
        }
    }

    @Test
    fun `should throw exception if name is too short`() {
        // Given
        val name = "a"

        // When & Then
        assertThrows<ShortNameException> {
            validator.checkName(name)
        }
    }
}