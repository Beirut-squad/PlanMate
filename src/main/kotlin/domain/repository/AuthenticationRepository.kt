package org.example.domain.repository

import org.example.data.model.User

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