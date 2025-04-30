package org.example.data.repositories.state_repository

import data.datasource.state_data_source.StateDataSource
import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State
import java.util.UUID


class StateRepositoryImpl(
        private val stateDataSource: StateDataSource
) : StateRepository {
    override fun createState(state: State): Result<State> {
        return if (state.name.isBlank()) {
            Result.failure(RuntimeException("Create failed : name is Blank !!"))
        }else{
            stateDataSource.createState(state)
        }
    }

    override fun editState(state: State): Result<State> {
       return if (state.name.isBlank()){
           Result.failure(RuntimeException("Edit failed : name is Blank !!"))
       }
        else{
            stateDataSource.editState(state)
       }
    }

    override fun deleteState(id: UUID): Result<Unit> {
        return try {
            stateDataSource.deleteState(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}