package domain.repository

import domain.model.User


interface AuthenticationRepository {
    suspend fun login(email: String, password: String): User
    suspend fun checkEmail(email: String)
    suspend fun registerMate(name: String, password: String, email: String): User
    suspend fun registerAdmin(name: String, password: String, email: String): User
    suspend fun logout()
    suspend fun isFirstRegister(): Boolean
    suspend fun getCurrentUser(): User
    suspend fun getUsers(): List<User>
}