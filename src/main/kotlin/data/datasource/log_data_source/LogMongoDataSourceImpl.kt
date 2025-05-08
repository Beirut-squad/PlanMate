package org.example.data.datasource.log_data_source

import com.mongodb.client.model.Filters
import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.logic.exceptions.log_exceptions.NoProjectLogsFoundException
import org.example.logic.exceptions.log_exceptions.NoTaskLogsFoundException
import org.example.models.*
import java.time.LocalDateTime
import java.util.*

class LogMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : LogDataSource {

    override suspend fun getProjectLogs(id: UUID): List<ProjectLog> = withContext(Dispatchers.IO) {
        val logs = mongoConnection.projectLogs.find(Filters.eq(ENTITY_ID_FIELD, id.toString())).toList()
        if (logs.isEmpty()) throw NoProjectLogsFoundException()
        logs.map { it.toProjectLog() }
    }

    override suspend fun getTaskLogs(id: UUID): List<TaskLog> = withContext(Dispatchers.IO) {
        val logs = mongoConnection.taskLogs.find(Filters.eq(ENTITY_ID_FIELD, id.toString())).toList()
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

    private fun ProjectLog.toDocument(): Document = Document()
        .append(ID_FIELD, id.toString())
        .append(USER_ID_FIELD, userId.toString())
        .append(ENTITY_ID_FIELD, entityId.toString())
        .append(PREVIOUS_ENTITY_FIELD, previousEntity?.toDocument())
        .append(CURRENT_ENTITY_FIELD, currentEntity?.toDocument())
        .append(CREATED_AT_FIELD, createdAt.toString())

    private fun TaskLog.toDocument(): Document = Document()
        .append(ID_FIELD, id.toString())
        .append(USER_ID_FIELD, userId.toString())
        .append(ENTITY_ID_FIELD, entityId.toString())
        .append(PREVIOUS_ENTITY_FIELD, previousEntity?.toDocument())
        .append(CURRENT_ENTITY_FIELD, currentEntity?.toDocument())
        .append(CREATED_AT_FIELD, createdAt.toString())

    private fun Document.toProjectLog(): ProjectLog = ProjectLog(
        id = UUID.fromString(getString(ID_FIELD)),
        userId = UUID.fromString(getString(USER_ID_FIELD)),
        entityId = UUID.fromString(getString(ENTITY_ID_FIELD)),
        previousEntity = get(PREVIOUS_ENTITY_FIELD, Document::class.java)?.toProject(),
        currentEntity = get(CURRENT_ENTITY_FIELD, Document::class.java)?.toProject(),
        createdAt = LocalDateTime.parse(getString(CREATED_AT_FIELD))
    )

    private fun Document.toTaskLog(): TaskLog = TaskLog(
        id = UUID.fromString(getString(ID_FIELD)),
        userId = UUID.fromString(getString(USER_ID_FIELD)),
        entityId = UUID.fromString(getString(ENTITY_ID_FIELD)),
        previousEntity = get(PREVIOUS_ENTITY_FIELD, Document::class.java)?.toTask(),
        currentEntity = get(CURRENT_ENTITY_FIELD, Document::class.java)?.toTask(),
        createdAt = LocalDateTime.parse(getString(CREATED_AT_FIELD))
    )

    private fun Project.toDocument(): Document {
        return Document(ID_FIELD, id.toString())
            .append(NAME_FIELD, name)
            .append(DESCRIPTION_FIELD, description)
            .append(CREATOR_USER_ID_FIELD, creatorUserID.toString())
            .append(CREATED_AT_FIELD, createdAt.toString())
            .append(UPDATED_AT_FIELD, updatedAt.toString())
            .append(STATE_FIELD, state.map {
                Document(ID_FIELD, it.id.toString()).append(NAME_FIELD, it.name)
            })
    }

    private fun Task.toDocument(): Document {
        return Document()

    }

    private fun Document.toStateList(): List<State> {
        return (this[STATE_FIELD] as? List<*>)?.mapNotNull { stateDoc ->
            (stateDoc as? Document)?.let {
                State(
                    id = UUID.fromString(it.getString(ID_FIELD)),
                    name = it.getString(NAME_FIELD)
                )
            }
        } ?: emptyList()
    }

    private fun Document.toProject(): Project {
        val states = this.toStateList()

        return Project(
            id = UUID.fromString(this.getString(ID_FIELD)),
            name = this.getString(NAME_FIELD),
            description = this.getString(DESCRIPTION_FIELD),
            creatorUserID = UUID.fromString(this.getString(CREATOR_USER_ID_FIELD)),
            createdAt = LocalDateTime.parse(this.getString(CREATED_AT_FIELD)),
            updatedAt = LocalDateTime.parse(this.getString(UPDATED_AT_FIELD)),
            state = states,
            users = emptyList()
        )
    }


    private fun Document.toTask(): Task {
        return Task(
            id = UUID.fromString(this.getString(ID_FIELD)),
            projectId = UUID.fromString(this.getString(PROJECT_ID_FIELD)),
            title = this.getString(TITLE_FIELD),
            description = this.getString(DESCRIPTION_FIELD),
            state = this.toStateList().firstOrNull() ?: State(UUID.randomUUID(), "No state"),
            creatorUserID = UUID.fromString(this.getString(CREATOR_USER_ID_FIELD)),
            createdAt = LocalDateTime.parse(this.getString(CREATED_AT_FIELD)),
            updatedAt = LocalDateTime.parse(this.getString(UPDATED_AT_FIELD))
        )
    }

    companion object {
        private const val ID_FIELD = "_id"
        private const val NAME_FIELD = "name"
        private const val DESCRIPTION_FIELD = "description"
        private const val CREATOR_USER_ID_FIELD = "creatorUserID"
        private const val UPDATED_AT_FIELD = "updatedAt"
        private const val STATE_FIELD = "state"
        private const val USER_ID_FIELD = "userId"
        private const val ENTITY_ID_FIELD = "entityId"
        private const val PREVIOUS_ENTITY_FIELD = "previousEntity"
        private const val CURRENT_ENTITY_FIELD = "currentEntity"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val PROJECT_ID_FIELD = "projectId"
        private const val TITLE_FIELD = "title"
    }
}
