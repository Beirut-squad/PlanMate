package org.example.data.datasource.authentication_data_source

import org.example.models.User
import java.util.UUID


interface AuthenticationDataSource {
    fun login(email: String, password: String): Result<User>
    fun checkEmail(email: String): Result<Unit>
    fun register(name: String, password: String, email: String): Result<User>
    fun registerAdmin(name: String, password: String, email: String): Result<User>
    fun logout(): Result<Unit>
    fun checkIfFirstRegister(): Result<Unit>
    fun getCurrentLoggedInUser(): Result<User?>
    fun getUsers(): Result<List<User>>
}