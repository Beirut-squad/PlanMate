package org.example.data.datasource.task_data_source

import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import logic.exceptions.task_management_exception.*
import org.bson.Document
import org.example.data.datasource.utils.toDocument
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

class TaskMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : TaskDataSource {

    override suspend fun createTask(task: Task) {
        withContext(Dispatchers.IO) {
                mongoConnection.tasks.insertOne(task.toDocument())
        }
    }
    override suspend fun editTask(task: Task) = withContext(Dispatchers.IO) {
            val updateResult = mongoConnection.tasks.updateOne(
                Document("_id", task.id.toString()),
                Document("\$set", task.toDocument())
            )

            if (updateResult.modifiedCount == 0L) {
                throw TaskEditException("Failed to edit task with id: ${task.id}")
            }
    }

    override suspend fun deleteTask(id: UUID) = withContext(Dispatchers.IO) {
            val deletedTask = mongoConnection.tasks.findOneAndDelete(Document("_id", id.toString()))
            if (deletedTask == null) {
                throw TaskDeletionException("No task found with id: $id")
            }

    }

    override suspend fun getAllTasks()= withContext(Dispatchers.IO) {

            val documents = mongoConnection.tasks.find().toList()

            if (documents.isEmpty()) {
                throw GetTaskException("No tasks found")
            }

            documents.map { doc ->
                val stateDoc = doc.get("state", Document::class.java)
                Task(
                    id = UUID.fromString(doc.getString("_id")),
                    projectId = UUID.fromString(doc.getString("projectId")),
                    title = doc.getString("title"),
                    description = doc.getString("description"),
                    state = State(
                        id = UUID.fromString(stateDoc.getString("_id")),
                        name = stateDoc.getString("name")
                    ),
                    creatorUserID = UUID.fromString(doc.getString("creatorUserID")),
                    createdAt = doc.get("createdAt", LocalDateTime::class.java),
                    updatedAt = doc.get("updatedAt", LocalDateTime::class.java)
                )
            }
    }

    override suspend fun getTask(id: UUID) = withContext(Dispatchers.IO) {
            val doc = mongoConnection.tasks.find(Document("_id", id.toString())).first()
                ?: throw GetTaskException("Task with ID $id not found")

            val stateDoc = doc.get("state", Document::class.java)

            Task(
                id = UUID.fromString(doc.getString("_id")),
                projectId = UUID.fromString(doc.getString("projectId")),
                title = doc.getString("title"),
                description = doc.getString("description"),
                state = State(
                    id = UUID.fromString(stateDoc.getString("_id")),
                    name = stateDoc.getString("name")
                ),
                creatorUserID = UUID.fromString(doc.getString("creatorUserID")),
                createdAt = doc.get("createdAt", LocalDateTime::class.java),
                updatedAt = doc.get("updatedAt", LocalDateTime::class.java)
            )

    }

    override suspend fun getTaskByStateIdAndProjectId(
        projectId: UUID,
        stateId: UUID
    ): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTasksForProject(projectId: UUID): List<Task> {
        TODO("Not yet implemented")
    }
}