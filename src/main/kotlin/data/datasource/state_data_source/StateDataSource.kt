package org.example.data.datasource.task_state_data_source

import org.example.models.State

interface StateDataSource {
    fun createTaskState(state: State)
    fun editTaskState(state: State)
    fun deleteTaskState(state: State)
}