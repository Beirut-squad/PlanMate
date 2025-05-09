package creator_helper

import org.example.data.model.Role
import org.example.data.model.User
import java.util.*

fun createUserHelper(
    name: String = "name",
    password: String = "password",
    email: String = "email",
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