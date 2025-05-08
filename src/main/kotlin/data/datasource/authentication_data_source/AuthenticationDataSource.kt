package org.example.data.datasource.authentication_data_source

import org.example.models.User


interface AuthenticationDataSource {
    suspend fun login(email: String, password: String)
    suspend fun checkEmail(email: String)
    suspend fun register(name: String, password: String, email: String): User
    suspend fun registerAdmin(name: String, password: String, email: String):User
    suspend fun logout()
    suspend fun checkIfFirstRegister()
    suspend fun getCurrentLoggedInUser():User?
}