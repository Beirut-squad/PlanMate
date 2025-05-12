package data.datasource.fake

import domain.exception.EmailAlreadyExistsException
import domain.exception.InvalidCredentialsException
import domain.exception.UserNotLoggedInException
import data.datasource.interfaces.AuthenticationDataSource
import domain.model.Role
import domain.model.User
import java.util.*

class AuthenticationDataSourceFake : AuthenticationDataSource {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): User {
        val user = users.find { it.email == email && it.password == password }
            ?: throw InvalidCredentialsException()
        currentUser = user
        return user

    }

    override suspend fun isValidEmail(email: String): Boolean {
        return !users.none { it.email == email }


    }

    override suspend fun registerMate(name: String, password: String, email: String): User {
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
            throw UserNotLoggedInException()
        }
        currentUser = null
    }

    override suspend fun isFirstRegister(): Boolean {
       return users.isNotEmpty()
    }

    override suspend fun getCurrentUser(): User {
        return currentUser ?: throw UserNotLoggedInException()
    }

    override suspend fun getUsers(): List<User> {
        return users
    }
}
