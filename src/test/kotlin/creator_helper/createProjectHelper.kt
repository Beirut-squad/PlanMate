package creator_helper

import org.example.models.Project
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

fun createProjectHelper(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    description: String = "",
    creatorUserID: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    states: List<State> = emptyList(),
): Project {
    return Project(
        id = id,
        name = name,
        description = description,
        creatorUserID = creatorUserID,
        createdAt = createdAt,
        updatedAt = updatedAt,
        states = states
    )
}