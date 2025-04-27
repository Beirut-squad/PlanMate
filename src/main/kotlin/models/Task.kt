package org.example.models

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: UUID,
    val projectId: String,
    val title: String,
    val description: String,
    val taskState: TaskState,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
): Loggable
