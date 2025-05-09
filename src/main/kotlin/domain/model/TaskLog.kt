package domain.model

import data.csv.model.Task
import java.time.LocalDateTime
import java.util.*

data class TaskLog(
    val id: UUID,
    val userId: UUID,
    val entityId: UUID,
    val previousEntity: Task?,
    val currentEntity: Task?,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
