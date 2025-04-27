package org.example.logic.repositories.authentication_repository

interface AuthenticationRepository {
    fun login(email: String, password: String)
    fun register(email: String, password: String)
    fun logout()
}