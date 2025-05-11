package org.example.data.datasource.mongo

import com.mongodb.client.model.Filters
import data.datasource.mapper.toDocument
import data.datasource.mapper.toTask
import org.example.data.datasource.mongo.mongo_db.MongoConnection
import domain.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.data.datasource.TaskDataSource
import org.example.domain.exceptions.task_management_exception.GetTaskException
import org.example.domain.exceptions.task_management_exception.TaskDeletionException
import org.example.domain.exceptions.task_management_exception.TaskEditException
import java.util.UUID

class TaskMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : TaskDataSource {

    override suspend fun createTask(task: Task) {
        withContext(Dispatchers.IO) {
            mongoConnection.tasks.insertOne(task.toDocument())
        }
    }

    override suspend fun editTask(task: Task) {
        withContext(Dispatchers.IO) {
            val updatedTask = mongoConnection.tasks.replaceOne(
                Filters.eq(ID_FILED, task.id.toString()),
                task.toDocument()
            )
            if (updatedTask.modifiedCount == 0L) {
                throw TaskEditException("Failed to edit task with id: ${task.id}")
            }
        }
    }

    override suspend fun deleteTask(id: UUID) {
        withContext(Dispatchers.IO) {
            val deletedTask = mongoConnection.tasks.findOneAndDelete(Document(ID_FILED, id.toString()))
            if (deletedTask == null) {
                throw TaskDeletionException("No task found with id: $id")
            }
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            val documents = mongoConnection.tasks.find().toList()

            documents.map {document ->
                document?.toTask() ?: throw Exception("No any tasks ")
            }
        }
    }

    override suspend fun getTask(id: UUID): Task {
        return withContext(Dispatchers.IO) {
            val document = mongoConnection.tasks.find(Document(ID_FILED, id.toString())).firstOrNull()
                ?: throw GetTaskException("Task with ID $id not found")

            document.toTask()
        }
    }

    override suspend fun getTasksByStateAndProjectIds(projectId: UUID, stateId: UUID): List<Task> {
        return withContext(Dispatchers.IO) {
            val filter = Document(PROJECT_ID_FILED, projectId.toString())
                .append(ID_FILED, stateId.toString())

            val documents = mongoConnection.tasks.find(filter).toList()

            documents.map {document ->
                document?.toTask() ?: throw Exception("Invalid task document")
            }
        }
    }

    override suspend fun getAllTasksForProject(projectId: UUID): List<Task> {
        return withContext(Dispatchers.IO) {
            val filter = Document(PROJECT_ID_FILED, projectId.toString())
            val documents = mongoConnection.tasks.find(filter).toList()

            documents.map {document ->
                document?.toTask() ?: throw Exception("Task document is null or invalid")
            }
        }
    }


    companion object {
        const val ID_FILED = "_id"
        const val PROJECT_ID_FILED = "projectId"
    }
}