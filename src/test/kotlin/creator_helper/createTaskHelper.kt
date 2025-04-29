package creator_helper

import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

fun createTaskHelper(
    id: UUID = UUID.randomUUID(),
    projectId: String = "test-project-id",
    title: String = "Test Task Title",
    description: String = "Test Task Description",
    state: State? = createTestTaskState(),
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


