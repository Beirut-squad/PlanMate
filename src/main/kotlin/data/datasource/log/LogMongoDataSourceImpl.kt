package org.example.data.datasource.log_data_source

import com.mongodb.client.model.Filters
import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.data.datasource.utils.*
import org.example.logic.exceptions.log_exceptions.NoProjectLogsFoundException
import org.example.logic.exceptions.log_exceptions.NoTaskLogsFoundException
import org.example.models.*
import java.util.*

class LogMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : LogDataSource {

    override suspend fun getProjectLogs(id: UUID): List<ProjectLog> = withContext(Dispatchers.IO) {
        val logs = mongoConnection.projectLogs.find(Filters.eq(ENTITY_ID_FILED, id.toString())).toList()
        if (logs.isEmpty()) throw NoProjectLogsFoundException()
        logs.map { it.toProjectLog() }
    }

    override suspend fun getTaskLogs(id: UUID): List<TaskLog> = withContext(Dispatchers.IO) {
        val logs = mongoConnection.taskLogs.find(Filters.eq(ENTITY_ID_FILED, id.toString())).toList()
        if (logs.isEmpty()) throw NoTaskLogsFoundException()
        logs.map { it.toTaskLog() }
    }

    override suspend fun saveProjectLog(projectLog: ProjectLog) {
        withContext(Dispatchers.IO) {
            mongoConnection.projectLogs.insertOne(projectLog.toDocument())
        }
    }

    override suspend fun saveTaskLog(taskLog: TaskLog) {
        withContext(Dispatchers.IO) {
            mongoConnection.taskLogs.insertOne(taskLog.toDocument())
        }
    }

    override suspend fun getAllProjectLogs(): List<ProjectLog> = withContext(Dispatchers.IO) {
        mongoConnection.projectLogs.find().toList().map { it.toProjectLog() }
    }

    override suspend fun getAllTaskLogs(): List<TaskLog> = withContext(Dispatchers.IO) {
        val logs = mongoConnection.taskLogs.find().toList()
        if (logs.isEmpty()) throw NoTaskLogsFoundException()
        logs.map { it.toTaskLog() }
    }



    companion object {
        private const val ENTITY_ID_FILED = "entityId"

    }
}
