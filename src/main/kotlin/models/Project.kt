package org.example.models

import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val id: UUID,
    var name: String,
    var description: String,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var taskState: List<TaskState>,
)
