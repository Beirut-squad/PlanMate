package org.example.data.datasource.task_data_source

import data.mongo_db.MongoConnection
import logic.exceptions.task_management_exception.GetTaskException
import logic.exceptions.task_management_exception.TaskDeletionException
import logic.exceptions.task_management_exception.TaskEditException
import org.bson.Document
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

class TaskMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
): TaskDataSource {
    override suspend fun createTask(task: Task): Result<Unit> {
        val docTask = Document("id", task.id.toString())
            .append("projectId", task.projectId.toString())
            .append("title", task.title)
            .append("description", task.description)
            .append("state", Document("id", task.state.id.toString()).append("name", task.state.name))
            .append("creatorUserID", task.creatorUserID.toString())
            .append("createdAt", task.createdAt)
            .append("updatedAt", task.updatedAt)

        return try{
            mongoConnection.tasks.insertOne(docTask)
            Result.success(Unit)
        } catch (e: Exception) {
            print("Error creating task: ${e.message}")
            throw e
        }
    }


    override suspend fun editTask(task: Task): Result<Unit> {
        return try {
            var findTasks = mongoConnection.tasks.find(Document("id", task.id.toString())).first()
            if (findTasks == null)
                return Result.failure((TaskEditException("Task not found with id: ${task.id}")))
            val updatedTask = Document()
                .append("projectId", task.projectId.toString())
                .append("title", task.title)
                .append("description", task.description)
                .append("state", Document("id", task.state.id.toString()).append("name", task.state.name))
                .append("creatorUserID", task.creatorUserID.toString())
                .append("createdAt", task.createdAt)
                .append("updatedAt", task.updatedAt)

            val updateResult = mongoConnection.tasks.updateOne(
                Document("id", task.id.toString()),
                Document("\$set", updatedTask)
            )
            if (updateResult.modifiedCount > 0) {
                return Result.success(Unit)
            } else
            Result.failure(TaskEditException("Failed to edit task"))

        } catch (e: Exception) {
        Result.failure(TaskEditException("Failed to edit task: ${e.message}"))
    }
    }

    override suspend fun deleteTask(id: UUID): Result<Unit> {
        return try {
            var deletedTask = mongoConnection.tasks.findOneAndDelete(Document("id" , id.toString()))
            if (deletedTask != null)
                Result.success(Unit)
            else
                Result.failure(TaskDeletionException("No task found with id: $id"))
        }catch (e: Exception) {
            Result.failure(TaskDeletionException("Failed to delete task: ${e.message}"))
        }
    }

    override suspend fun getAllTasks(): Result<List<Task>> {
        return try {
            val documents = mongoConnection.tasks.find().toList()

            if (documents.isEmpty()) {
                Result.failure(GetTaskException("No tasks found"))
            } else {
                val tasks = documents.map { doc ->
                    Task(
                        id = UUID.fromString(doc.getString("id")),
                        projectId = UUID.fromString(doc.getString("projectId")),
                        title = doc.getString("title"),
                        description = doc.getString("description"),
                        state = State(
                            id = UUID.randomUUID(),
                            name = doc.getString("state")
                        ),
                        creatorUserID = UUID.fromString(doc.getString("creatorUserID")),
                        createdAt = doc.get("createdAt", LocalDateTime::class.java),
                        updatedAt = doc.get("updatedAt", LocalDateTime::class.java)
                    )
                }
                Result.success(tasks)
            }
        } catch (e: Exception) {
            Result.failure(GetTaskException("Failed to retrieve tasks: ${e.message}"))
        }
    }



    override suspend fun getTask(id: UUID): Result<Task> {
        return try {
            val doc = mongoConnection.tasks.find(Document("id", id.toString())).first()

            if (doc != null) {
                val task = Task(
                        id = UUID.fromString(doc.getString("id")),
                        projectId = UUID.fromString(doc.getString("projectId")),
                        title = doc.getString("title"),
                        description = doc.getString("description"),
                        state = State(
                            id = UUID.randomUUID(),
                            name = doc.getString("state")
                        ),
                        creatorUserID = UUID.fromString(doc.getString("creatorUserID")),
                        createdAt = doc.get("createdAt", LocalDateTime::class.java),
                        updatedAt = doc.get("updatedAt", LocalDateTime::class.java)
                    )
                Result.success(task)
                }
            else
                Result.failure(GetTaskException("Task with ID $id not found"))
        } catch (e: Exception) {
            Result.failure(GetTaskException("Failed to retrieve tasks: ${e.message}"))
        }
    }

}