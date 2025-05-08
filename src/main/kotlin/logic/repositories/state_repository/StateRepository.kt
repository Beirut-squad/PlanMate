package org.example.logic.repositories.state_repository

import org.example.models.State
import java.util.UUID

interface StateRepository {
    fun createState(state: State): State
    fun editState(state: State): State
    fun deleteState(id: UUID)
}