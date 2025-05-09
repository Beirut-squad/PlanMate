package org.example.data.model

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: UUID,
    val projectId: UUID,
    val title: String,
    val description: String,
    val state: State,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
