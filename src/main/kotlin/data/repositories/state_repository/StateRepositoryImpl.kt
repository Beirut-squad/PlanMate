package org.example.data.repositories.state_repository

import data.datasource.state_data_source.StateDataSource
import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State
import java.util.UUID


class StateRepositoryImpl(
        private val stateDataSource: StateDataSource
) : StateRepository {
    override fun createState(state: State): Result<State> {
        TODO("Not yet implemented")
    }

    override fun editState(state: State): Result<State> {
        TODO("Not yet implemented")
    }

    override fun deleteState(id: UUID): Result<Unit> {
        TODO("Not yet implemented")
    }

}