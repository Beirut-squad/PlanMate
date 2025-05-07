package org.example.data.repositories.state_repository

import data.datasource.state_data_source.StateDataSource
import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State
import java.util.UUID


class StateRepositoryImpl(
        private val stateDataSource: StateDataSource
) : StateRepository {
    override fun createState(state: State): State {
        return stateDataSource.createState(state)
    }

    override fun editState(state: State): State {
       return stateDataSource.editState(state)
    }

    override fun deleteState(id: UUID): Unit {
         stateDataSource.deleteState(id)
    }
}