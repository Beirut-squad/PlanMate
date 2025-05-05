package org.example.data.repositories.state_repository

import data.datasource.state_data_source.StateDataSource
import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State
import java.util.UUID


class StateRepositoryImpl(
        private val stateDataSource: StateDataSource
) : StateRepository {
    override fun createState(state: State): Result<State> {
        return stateDataSource.createState(state)
    }

    override fun editState(state: State): Result<State> {
       return stateDataSource.editState(state)
    }

    override fun deleteState(id: UUID): Result<Unit> {
        return stateDataSource.deleteState(id)
    }

    override fun getState(projectId: UUID): Result<State> {
        return stateDataSource.getState(projectId)
    }
}