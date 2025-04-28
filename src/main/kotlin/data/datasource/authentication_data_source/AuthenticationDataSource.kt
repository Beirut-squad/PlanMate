package org.example.data.datasource.authentication_data_source

import org.example.models.User

interface AuthenticationDataSource {
    fun login(name: String, password: String)
    fun register(user: User)
    fun logout()
}