package org.example.models

import java.time.LocalDateTime
import java.util.UUID

data class ProjectLog(
    val id: UUID,
    val userId: UUID,
    val entityId: UUID,
    val previousEntity: Project?,
    val currentEntity: Project?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)