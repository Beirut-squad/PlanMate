package org.example.models

import java.util.UUID

data class State(
    val id: UUID,
    val projectId: UUID,
    var name: String
)
