package org.example.data.datasource.task_data_source

import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import logic.exceptions.task_management_exception.*
import org.bson.Document
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

class TaskMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : TaskDataSource {

    override suspend fun createTask(task: Task) {
        withContext(Dispatchers.IO) {
            val docTask = Document("_id", task.id.toString())
                .append("projectId", task.projectId.toString())
                .append("title", task.title)
                .append("description", task.description)
                .append("state", Document("_id", task.state.id.toString()).append("name", task.state.name))
                .append("creatorUserID", task.creatorUserID.toString())
                .append("createdAt", task.createdAt)
                .append("updatedAt", task.updatedAt)

            try {
                mongoConnection.tasks.insertOne(docTask)
            } catch (e: Exception) {
                throw TaskCreationException("Error creating task: ${e.message}")
            }
        }
    }
    override suspend fun editTask(task: Task) = withContext(Dispatchers.IO) {
        try {
            val findTasks = mongoConnection.tasks.find(Document("_id", task.id.toString())).first()
                ?: throw TaskEditException("Task not found with id: ${task.id}")

            val updatedTask = Document()
                .append("projectId", task.projectId.toString())
                .append("title", task.title)
                .append("description", task.description)
                .append("state", Document("_id", task.state.id.toString()).append("name", task.state.name))
                .append("creatorUserID", task.creatorUserID.toString())
                .append("createdAt", task.createdAt)
                .append("updatedAt", task.updatedAt)

            val updateResult = mongoConnection.tasks.updateOne(
                Document("_id", task.id.toString()),
                Document("\$set", updatedTask)
            )

            if (updateResult.modifiedCount == 0L) {
                throw TaskEditException("Failed to edit task with id: ${task.id}")
            }
        } catch (e: Exception) {
            throw TaskEditException("Failed to edit task: ${e.message}")
        }
    }

    override suspend fun deleteTask(id: UUID) = withContext(Dispatchers.IO) {
        try {
            val deletedTask = mongoConnection.tasks.findOneAndDelete(Document("_id", id.toString()))
            if (deletedTask == null) {
                throw TaskDeletionException("No task found with id: $id")
            }
        } catch (e: Exception) {
            throw TaskDeletionException("Failed to delete task: ${e.message}")
        }
    }

    override suspend fun getAllTasks()= withContext(Dispatchers.IO) {
        try {
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
        } catch (e: Exception) {
            throw GetTaskException("Failed to retrieve tasks: ${e.message}")
        }
    }

    override suspend fun getTask(id: UUID) = withContext(Dispatchers.IO) {
        try {
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
        } catch (e: Exception) {
            throw GetTaskException("Failed to retrieve task: ${e.message}")
        }
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