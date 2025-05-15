package creator_helper

import domain.model.UserRole
import domain.model.User
import java.util.*

fun createUserHelper(
    name: String = "name",
    password: String = "password",
    email: String = "email",
    id: UUID = UUID.randomUUID(),
    userRole: UserRole = UserRole.MATE,
    isDeleted: Boolean = false,
): User {
    return User(
        name = name,
        password = password,
        email =email,
        id = id,
        userRole = userRole,
        isDeleted = isDeleted,
    )
}

val adminUser = createUserHelper(
    name = "admin",
    email = "admin@gmail.com",
    password = "admin123",
    userRole = UserRole.ADMIN
)

val mateUser = createUserHelper(
    name = "mate",
    email = "mate@gmail.com",
    password = "mate123",
    userRole = UserRole.MATE
)