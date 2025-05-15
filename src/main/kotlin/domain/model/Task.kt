package domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val projectId: UUID,
    val title: String,
    val description: String,
    val taskState: TaskState,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
