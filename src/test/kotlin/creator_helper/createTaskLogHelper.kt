package creator_helper

import org.example.models.Project
import org.example.models.ProjectLog
import org.example.models.Task
import org.example.models.TaskLog
import java.time.LocalDateTime
import java.util.*

fun createTaskLogHelper(
    userId: UUID = UUID.randomUUID(),
    entityId: UUID = UUID.randomUUID(),
    previousEntity: Task? = null,
    currentEntity: Task? = null
): TaskLog {
    return TaskLog(
        id = UUID.randomUUID(),
        userId = userId,
        entityId = entityId,
        previousEntity = previousEntity,
        currentEntity = currentEntity,
        createdAt = LocalDateTime.now(),
    )
}