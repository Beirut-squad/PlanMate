package org.example.logic.repositories.authentication_repository

import org.example.models.User
import java.util.*

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): User
    suspend fun checkEmail(email: String): Unit
    suspend fun register(name: String, password: String, email: String): User
    suspend fun registerAdmin(name: String, password: String, email: String): User
    suspend fun logout()
    suspend fun checkIfFirstRegister()
    suspend fun getCurrentLoggedInUser(): User?
    suspend fun getUsers(): List<User>
}