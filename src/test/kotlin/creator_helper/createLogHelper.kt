package creator_helper

import org.example.models.EntityType
import org.example.models.Log
import org.example.models.Loggable
import java.time.LocalDateTime
import java.util.*

fun createTestLog(
    id: UUID = UUID.randomUUID(),
    entityType: EntityType = EntityType.TASK,
    userID: UUID = UUID.randomUUID(),
    previousEntity: Loggable? = null,
    currentEntity: Loggable? = null,
    createdAt: LocalDateTime = LocalDateTime.now()
): Log {
    return Log(
        id = id,
        entityType = entityType,
        userID = userID,
        previousEntity = previousEntity,
        currentEntity = currentEntity,
        createdAt = createdAt
    )
}