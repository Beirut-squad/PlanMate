package org.example.models

import java.time.LocalDateTime
import java.util.UUID

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val state: State,
    val creatorUserID: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
