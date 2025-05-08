package org.example.fake_datasource

import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.logic.exceptions.authentication_exceptions.EmailAlreadyExistsException
import org.example.logic.exceptions.authentication_exceptions.InvalidEmailOrPasswordException
import org.example.logic.exceptions.authentication_exceptions.NoLoggedInUserException
import org.example.logic.exceptions.authentication_exceptions.UsersAlreadyExistException
import org.example.models.Role
import org.example.models.User
import java.util.*

class AuthenticationDataSourceFakeImpl : AuthenticationDataSource {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): User {
        val user = users.find { it.email == email && it.password == password }
            ?: throw InvalidEmailOrPasswordException()
        currentUser = user
        return user

    }

    override suspend fun checkEmail(email: String) {
        if (users.none { it.email == email }) {
            throw Exception("User with email $email not found")
        }

    }

    override suspend fun register(name: String, password: String, email: String): User {
         if (users.any { it.email == email }) {
            throw EmailAlreadyExistsException()
        }else{
             val user = User(
                 id = UUID.randomUUID(),
                 name = name,
                 password = password,
                 email = email,
                 role = Role.MATE
             )
             users.add(user)
             currentUser = user // I add this line to make user when register currentUser
             return user
        }
    }

    override suspend fun registerAdmin(name: String, password: String, email: String): User {
        if (users.any { it.email == email }) {
            throw EmailAlreadyExistsException()
        } else {
            val user = User(
                id = UUID.randomUUID(),
                name = name,
                password = password,
                email = email,
                role = Role.ADMIN
            )
            users.add(user)
            currentUser = user
            return user
        }
    }

    override suspend fun logout(){
        if (currentUser == null) {
            throw NoLoggedInUserException()
        }
        currentUser = null
    }

    override suspend fun checkIfFirstRegister() {
       if (users.isNotEmpty()) {
            throw UsersAlreadyExistException()
        }
    }

    override suspend fun getCurrentLoggedInUser(): User? {
        return currentUser
    }

    override suspend fun getUsers(): List<User> {
        return users
    }
}

// https://meet.google.com/ida-kuzh-khe