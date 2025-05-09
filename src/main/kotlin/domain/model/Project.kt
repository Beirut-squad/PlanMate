package domain.model

import data.csv.model.State
import data.csv.model.User
import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val id: UUID,
    var title: String,
    var description: String,
    val creatorUserID: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val users: List<User>,
    val state: List<State>)
