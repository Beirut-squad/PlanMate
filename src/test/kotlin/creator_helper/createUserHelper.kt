package creator_helper

import org.example.models.Role
import org.example.models.User
import java.util.*

fun createUserHelper(
     name: String = "",
     password: String = "",
     email: String = "",
     id: UUID = UUID.randomUUID(),
     role: Role = Role.MATE,
     isDeleted: Boolean = false,
): User {
    return User(
        name = name,
        password = password,
        email =email,
        id = id,
        role = role,
        isDeleted = isDeleted,
    )
}