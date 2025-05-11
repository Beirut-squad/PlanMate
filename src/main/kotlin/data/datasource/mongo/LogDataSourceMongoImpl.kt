package data.datasource.mongo

import com.mongodb.client.model.Filters
import data.exception.NoProjectLogsFoundException
import data.exception.NoTaskLogsFoundException
import data.datasource.mapper.toDocument
import data.datasource.mapper.toProjectLog
import data.datasource.mapper.toTaskLog
import org.example.data.datasource.mongo.mongo_db.MongoConnection
import domain.model.ProjectLog
import domain.model.TaskLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.data.datasource.LogDataSource
import java.util.UUID

class LogDataSourceMongoImpl(
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