package org.example.data.datasource.authentication_data_source

import org.example.models.User


interface AuthenticationDataSource {
    fun login(email: String, password: String): Result<User>
    fun register(name: String, password: String, email: String): Result<User>
    fun registerAdmin(name: String, password: String, email: String): Result<User>
    fun logout(): Result<Unit>
    fun checkIfFirstRegister(): Result<Unit>
}