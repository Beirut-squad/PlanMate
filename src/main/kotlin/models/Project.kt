package org.example.models

import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val id: UUID,
    val name: String,
    val description: String,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val state: List<State>,
) : Loggable
