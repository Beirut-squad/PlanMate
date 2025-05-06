package org.example.data.datasource.state_data_source

import data.datasource.state_data_source.StateDataSource
import data.mongo_db.MongoConnection
import org.example.models.State
import org.w3c.dom.Document
import java.util.*

class StateMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
): StateDataSource {
    override suspend fun createState(state: State): Result<State> {
      val docState =  Document("id", )

    }

    override fun editState(state: State): Result<State> {
        TODO("Not yet implemented")
    }

    override fun deleteState(id: UUID): Result<Unit> {
        TODO("Not yet implemented")
    }
}