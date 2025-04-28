package org.example.logic.repositories.authentication_repository

import org.example.models.Role
import org.example.models.User

interface AuthenticationRepository {
    fun login(email: String, password: String)
    fun register(user: User)
    fun logout()
}