package creator_helper

import java.util.UUID

fun createCsvLineForUser(

    id: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
    name: String = "Ismail",
    password: String = "secret123",
    email: String = "ismail.elkalili@gmail.com",
    role: String = "ADMIN",
    isDeleted: Boolean = false
): String {
    return "$id,$name,$password,$email,$role,$isDeleted"
}

fun createCsvLineForUserInvalid(

    id: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
    name: String = "",
    password: String = "secret123",
    email: String = "ismail.elkalili@gmail.com",
    role: String = "ADMIN",
    isDeleted: Boolean = false
): String {
    return "$id,$name,$password,$email,$role,$isDeleted"
}



