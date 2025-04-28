package org.example.logic.repositories.state_repository

import org.example.models.State

interface StateRepository {
    fun createTaskState(state: State)
    fun editTaskState(state: State)
    fun deleteTaskState(state: State)
}