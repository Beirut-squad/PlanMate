package data.datasource.state_data_source

import org.example.models.State
import java.util.UUID

interface StateDataSource {
    suspend fun createState(state: State)
    suspend fun editState(state: State)
    suspend fun deleteState(id: UUID)
}