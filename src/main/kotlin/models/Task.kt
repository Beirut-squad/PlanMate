package org.example.models

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: UUID,
    val projectId: UUID,
    var title: String,
    var description: String,
    var taskState: TaskState,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
