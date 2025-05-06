package org.example.data.datasource.state_data_source

import data.datasource.state_data_source.StateDataSource
import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import logic.exceptions.task_management_exception.StateException
import logic.exceptions.task_management_exception.TaskDeletionException
import org.example.models.State
import org.bson.Document
import java.util.*

class StateMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
): StateDataSource {
    override suspend fun createState(state: State) {
        withContext(Dispatchers.IO) {
            try {
                val document = Document("id", state.id.toString())
                    .append("name", state.name)

                mongoConnection.states.insertOne(document)
            } catch (e: Exception) {
                throw StateException("Failed to create state: ${e.message}")
            }
        }
    }

    override suspend fun editState(state: State) = withContext(Dispatchers.IO) {
        try {
            val filter = Document("id", state.id.toString())
            val update = Document("\$set", Document("name", state.name))

            val updateState = mongoConnection.states.updateOne(filter, update)
            if (updateState.modifiedCount == 0L)
                throw StateException("Failed to edit task with id: ${state.id}")

        }
        catch (e: Exception) {
            throw StateException("Failed to edit task: ${e.message}")
        }
    }

    override suspend fun deleteState(id: UUID) = withContext(Dispatchers.IO) {
        try {
            val deletedState = mongoConnection.states.findOneAndDelete(Document("id", id.toString()))
            if (deletedState == null) {
                throw StateException("No state found with id: $id")
            }
        } catch (e: Exception) {
            throw StateException("Failed to delete state: ${e.message}")
        }
    }

}