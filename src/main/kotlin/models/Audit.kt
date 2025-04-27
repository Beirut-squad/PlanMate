package org.example.models

import java.time.LocalDateTime

data class Audit(
    val id:String,
    val entityType: EntityType, // PROJECT or TASK
    val entityID: String, // ProjectID or TaskID
    val userID: String,
    val oldState: State,
    val newState: State,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    )
enum class EntityType {
 PROJECT , TASK
}
