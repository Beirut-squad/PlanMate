package data.datasource.authentication

import domain.model.User

interface AuthenticationDataSource {
    suspend fun login(email: String, password: String): User
    suspend fun isValidEmail(email: String): Boolean
    suspend fun register(name: String, password: String, email: String): User
    suspend fun registerAdmin(name: String, password: String, email: String): User
    suspend fun logout()
    suspend fun isFirstRegister(): Boolean
    suspend fun getCurrentUser(): User
    suspend fun getUsers(): List<User>
}