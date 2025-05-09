package creator_helper

import data.csv.model.Project
import data.csv.model.State
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
): Project {
    return Project(
        id = id,
        name = name,
        description = description,
        creatorUserID = creatorUserID,
        createdAt = createdAt,
        updatedAt = updatedAt,
        users = emptyList(),
        state = state
    )
}