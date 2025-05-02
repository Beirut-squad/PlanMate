package org.example.data.datasource.task_data_source

import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.GetAllTasksException
import org.example.logic.exceptions.GetTaskException
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.exceptions.TaskDeletionException
import org.example.logic.exceptions.TaskEditException
import org.example.models.Task
import java.util.UUID

class TaskDataSourceImpl(
    private val csvReader: CsvReader<Task>,
    private val csvWriter: CsvWriter<Task>,
) : TaskDataSource {
    override fun createTask(task: Task): Result<Unit> {
        return try {
            val currentTasks = csvReader.read(TASK_FILE)
            val updatedTasks = currentTasks.toMutableList().apply {
                add(task)
            }
            csvWriter.writeToFile(updatedTasks, TASK_FILE)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(TaskCreationException("Failed to create task: ${exception.message}"))
        }
    }

    override fun editTask(task: Task): Result<Unit> {
        return try {
            val currentTasks = csvReader.read(TASK_FILE)
            val taskIndex = currentTasks.indexOfFirst { it.id == task.id }
            if (currentTasks.isEmpty()) {
                return Result.failure(TaskEditException("No tasks found to edit"))
            }
            if (taskIndex == -1) {
                return Result.failure(TaskEditException("Task with ID ${task.id} not found"))
            }
            val updatedTasks = currentTasks.toMutableList().apply {
                this[taskIndex] = task
            }
            csvWriter.writeToFile(updatedTasks, TASK_FILE)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(TaskEditException("Failed to edit task: ${exception.message}"))
        }
    }

    override fun deleteTask(id: UUID): Result<Unit> {
        return try {
            val currentTasks = csvReader.read(TASK_FILE)
            val taskIndex = currentTasks.indexOfFirst { it.id == id }

            if (currentTasks.isEmpty()) {
                return Result.failure(TaskDeletionException("No tasks found to delete"))
            }

            if (taskIndex == -1) {
                return Result.failure(TaskDeletionException("Failed to delete task"))
            }
            val updatedTasks = currentTasks.toMutableList().apply {
                removeAt(taskIndex)
            }
            csvWriter.writeToFile(updatedTasks, TASK_FILE)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(TaskDeletionException("Failed to delete task ${e.message}"))
        }
    }

    override fun getAllTasks(): Result<List<Task>> {
        return try {
            val tasks = csvReader.read(TASK_FILE)
            if (tasks.isEmpty()) {
                Result.failure(GetAllTasksException("No tasks found"))
            } else {
                Result.success(tasks)
            }
        } catch (e: Exception) {
            Result.failure(GetAllTasksException("Failed to read tasks: ${e.message}"))
        }
    }

    override fun getTask(id: UUID): Result<Task> {
        return try {
            val tasks = csvReader.read(TASK_FILE)
            val task = tasks.find { it.id == id }
            if (task != null) {
                Result.success(task)
            } else {
                Result.failure(GetTaskException("Task with ID $id not found"))
            }
        } catch (e: Exception) {
            Result.failure(GetTaskException("Failed to retrieve task: ${e.message}"))
        }
    }

    companion object {
        const val TASK_FILE = "tasks.csv"
    }
}