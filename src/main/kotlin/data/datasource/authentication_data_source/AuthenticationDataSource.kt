package org.example.data.datasource.authentication_data_source

import org.example.models.User
import java.util.UUID


interface AuthenticationDataSource {
    suspend fun login(email: String, password: String): User
    suspend fun checkEmail(email: String)
    suspend fun register(name: String, password: String, email: String): User
    suspend fun registerAdmin(name: String, password: String, email: String): User
    suspend fun logout()
    suspend fun checkIfFirstRegister()
    suspend fun getCurrentLoggedInUser(): User?
    suspend fun getUsers(): List<User>
}