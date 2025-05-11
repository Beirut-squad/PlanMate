package creator_helper

import domain.model.State
import domain.model.Task
import java.time.LocalDateTime
import java.util.*

fun createTaskHelper(
    id: UUID = UUID.randomUUID(),
    projectId: UUID = UUID.randomUUID(),
    title: String = "Test Task Title",
    description: String = "Test Task Description",
    state: State = createStateHelper(),
    creatorUserID: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now()
): Task {
    return Task(
        id = id,
        projectId = projectId,
        title = title,
        description = description,
        state = state,
        creatorUserID = creatorUserID,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}


