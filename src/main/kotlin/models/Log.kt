package org.example.models

import java.time.LocalDateTime
import java.util.UUID

data class Log(
    val id: UUID,
    val entityType: EntityType, // PROJECT or TASK
    val userID: UUID,
    val previousEntity: Loggable?,
    val currentEntity: Loggable?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

enum class EntityType {
    PROJECT, TASK
}
