package creator_helper

import domain.model.Role
import domain.model.User
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

val adminUser = createUserHelper(
    name = "admin",
    email = "admin@gmail.com",
    password = "admin123",
    role = Role.ADMIN
)

val mateUser = createUserHelper(
    name = "mate",
    email = "mate@gmail.com",
    password = "mate123",
    role = Role.MATE
)