package org.example.data.datasource.authentication_data_source


interface AuthenticationDataSource {
    fun login(email: String, password: String)
    fun register(name: String, password: String, email:String)
    fun logout()
}