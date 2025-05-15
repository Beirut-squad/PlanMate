package creator_helper

import domain.model.Project
import domain.model.TaskState
import domain.model.User
import java.time.LocalDateTime
import java.util.*

fun createProjectHelper(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Project Name",
    description: String = "Test Project Description",
    creatorUserID: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    taskState: List<TaskState> = listOf(createStateHelper()),
    users: List<User> = listOf(createUserHelper())
): Project {
    return Project(
        id = id,
        title = name,
        description = description,
        creatorUserID = creatorUserID,
        createdAt = createdAt,
        updatedAt = updatedAt,
        users = users,
        taskStates = taskState
    )
}