package data.datasource.state_data_source

import org.example.models.State
import java.util.UUID

interface StateDataSource {
    fun createState(state: State): Result<State>
    fun editState(state: State):Result<State>
    fun deleteState(id: UUID):Result<Unit>
}