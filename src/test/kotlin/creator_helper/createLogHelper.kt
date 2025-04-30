package creator_helper

import org.example.models.EntityType
import org.example.models.Log
import org.example.models.Loggable
import java.time.LocalDateTime
import java.util.*

fun createTestLog(
    entityType: EntityType = EntityType.TASK,
    userID: UUID = UUID.randomUUID(),
    previousEntity: Loggable? = null,
    currentEntity: Loggable? = null
): Log {
    return Log(
        id = UUID.randomUUID(),
        entityType = entityType,
        userID = userID,
        previousEntity = previousEntity,
        currentEntity = currentEntity,
        createdAt = LocalDateTime.now()
    )
}