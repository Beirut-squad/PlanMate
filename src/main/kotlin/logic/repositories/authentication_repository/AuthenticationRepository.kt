package org.example.logic.repositories.authentication_repository

import org.example.models.Role
import org.example.models.User

interface AuthenticationRepository {
    fun login(email: String, password: String)
    fun register(name: String, password: String, email: String)
    fun logout()
}