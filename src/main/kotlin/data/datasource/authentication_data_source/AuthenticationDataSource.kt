package org.example.data.datasource.authentication_data_source

import org.example.models.User


interface AuthenticationDataSource {
    suspend fun login(email: String, password: String)
    suspend fun checkEmail(email: String): Result<Unit>
    suspend fun register(name: String, password: String, email: String): Result<User>
    suspend fun registerAdmin(name: String, password: String, email: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun checkIfFirstRegister(): Result<Unit>
    suspend fun getCurrentLoggedInUser(): Result<User?>
}