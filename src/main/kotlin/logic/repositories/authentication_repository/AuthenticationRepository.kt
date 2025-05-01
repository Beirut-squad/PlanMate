package org.example.logic.repositories.authentication_repository

import org.example.models.User

interface AuthenticationRepository {
    fun login(email: String, password: String): Result<User>
    fun checkEmail(email: String): Result<Unit>
    fun register(name: String, password: String, email: String): Result<User>
    fun registerAdmin(name: String, password: String, email: String): Result<User>
    fun logout(): Result<Unit>
    fun checkIfFirstRegister(): Result<Unit>
    fun getCurrentLoggedInUser(): Result<User?>
}