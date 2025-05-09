package domain.model

import org.example.data.model.Role
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val password: String,
    val email: String,
    val role: Role,
    val isDeleted: Boolean = false
)

enum class Role {
    ADMIN, MATE
}


