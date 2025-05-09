package creator_helper

import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.time.LocalDateTime
import java.util.*

fun createProjectHelper(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Project Name",
    description: String = "Test Project Description",
    creatorUserID: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    state: List<State> = listOf(createStateHelper()),
    users: List<User> = emptyList()
): Project {
    return Project(
        id = id,
        name = name,
        description = description,
        creatorUserID = creatorUserID,
        createdAt = createdAt,
        updatedAt = updatedAt,
        users = users,
        state = state
    )
}