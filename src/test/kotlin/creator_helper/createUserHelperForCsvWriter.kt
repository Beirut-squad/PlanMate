package creator_helper

import org.example.data.model.Role
import org.example.data.model.User
import java.util.*

fun createUserForCsvWriter(
    id: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
    name: String = "Ismail",
    password: String = "secret123",
    email: String = "ismail.elkalili@gmail.com",
    role: String = "ADMIN",
    isDeleted: Boolean = false
): User {
    return User(
        name = name,
        password = password,
        email =email,
        id = id,
        role = Role.valueOf(role),
        isDeleted = isDeleted,
    )
}

fun createUserForCsvWriterInvalid(

    id: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
    name: String = "",
    password: String = "secret123",
    email: String = "ismail.elkalili@gmail.com",
    role: String = "ADMIN",
    isDeleted: Boolean = false
): User {
    return User(
        name = name,
        password = password,
        email =email,
        id = id,
        role = Role.valueOf(role),
        isDeleted = isDeleted,
    )
}
