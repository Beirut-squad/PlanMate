package creator_helper

import org.example.models.Project
import org.example.models.ProjectLog
import java.time.LocalDateTime
import java.util.*

fun createProjectLogHelper(
    userId: UUID = UUID.randomUUID(),
    entityId: UUID = UUID.randomUUID(),
    previousEntity: Project? = null,
    currentEntity: Project? = null
): ProjectLog {
    return ProjectLog(
        id = UUID.randomUUID(),
        userId = userId,
        entityId = entityId,
        previousEntity = previousEntity,
        currentEntity = currentEntity,
        createdAt = LocalDateTime.now(),
    )
}