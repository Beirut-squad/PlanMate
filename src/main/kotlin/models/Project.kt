package org.example.models

import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime

data class Project(
    val id:String,
    val name:String,
    val description:String,
    val creatorUserID: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val state: State,
)
