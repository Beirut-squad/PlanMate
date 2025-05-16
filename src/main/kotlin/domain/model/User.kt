package domain.model

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val password: String,
    val email: String,
    val userRole: UserRole,
    val isDeleted: Boolean = false
)

enum class UserRole {
    ADMIN, MATE
}


