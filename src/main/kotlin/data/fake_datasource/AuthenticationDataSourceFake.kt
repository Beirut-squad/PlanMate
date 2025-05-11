package org.example.data.fake_datasource

import org.example.data.datasource.AuthenticationDataSource
import domain.exception.authentication.EmailAlreadyExistsException
import domain.exception.authentication.InvalidEmailOrPasswordException
import domain.exception.authentication.NoLoggedInUserException
import domain.exception.authentication.UsersAlreadyExistException
import domain.model.Role
import domain.model.User
import java.util.*

class AuthenticationDataSourceFake : AuthenticationDataSource {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): User {
        val user = users.find { it.email == email && it.password == password }
            ?: throw InvalidEmailOrPasswordException()
        currentUser = user
        return user

    }

    override suspend fun isValidEmail(email: String): Boolean {
        return !users.none { it.email == email }


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
             currentUser = user
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

    override suspend fun isFirstRegister(): Boolean {
       return users.isEmpty()
    }

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

    override suspend fun getUsers(): List<User> {
        return users
    }
}
