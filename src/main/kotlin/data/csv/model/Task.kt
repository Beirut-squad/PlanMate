package data.csv.model

import data.csv.model.State
import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: UUID,
    val projectId: UUID,
    val name: String,
    val description: String,
    val state: State,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
