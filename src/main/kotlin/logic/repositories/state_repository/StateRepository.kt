package org.example.logic.repositories.state_repository

import org.example.models.State
import java.util.UUID

interface StateRepository {
    fun createState(state: State): Result<State>
    fun editState(state: State): Result<State>
    fun deleteState(id: UUID): Result<Unit>
    fun getState(projectId: UUID): Result<State>
}