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
import kotlin.jvm.Throws

class TaskDataSourceImpl(
    private val csvReader: CsvReader<Task>,
    private val csvWriter: CsvWriter<Task>,
) : TaskDataSource {


    override fun createTask(task: Task): Result<Unit> {
        return try {
            val currentTasks = readTasksFromFile()
            val updatedTasks = addTaskToList(currentTasks, task)
            writeTasksToFile(updatedTasks)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(TaskCreationException("Failed to create task: ${exception.message}"))
        }
    }

    override fun editTask(task: Task): Result<Unit> {
        return try {
            val currentTasks = readTasksFromFile()
            validateTasksExist(currentTasks, "No tasks found to edit")

            val taskIndex = findTaskIndexById(currentTasks, task.id)
            validateTaskExists(taskIndex, task.id, "No tasks found to edit")

            val updatedTasks = updateTaskInList(currentTasks, taskIndex, task)
            writeTasksToFile(updatedTasks)

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(TaskEditException("Failed to edit task: ${exception.message}"))
        }
    }

    override fun deleteTask(id: UUID): Result<Unit> {
        return try {
            val currentTasks = readTasksFromFile()
            validateTasksExist(currentTasks, "No tasks found to delete")

            val taskIndex = findTaskIndexById(currentTasks, id)
            validateTaskExists(taskIndex, id, "Cannot delete task")

            val updatedTasks = removeTaskFromList(currentTasks, taskIndex)
            writeTasksToFile(updatedTasks)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(TaskDeletionException("Failed to delete task: ${e.message}"))
        }
    }

    override fun getAllTasks(): Result<List<Task>> {
        return try {
            val tasks = readTasksFromFile()
            validateTasksExist(tasks, "No tasks found")

            Result.success(tasks)

        } catch (e: Exception) {
            Result.failure(GetAllTasksException("Failed to read tasks: ${e.message}"))
        }
    }

    override fun getTask(id: UUID): Result<Task> {
        return try {
            val tasks = readTasksFromFile()
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

    private fun readTasksFromFile(): List<Task> {
        return csvReader.read(TASK_FILE)
    }

    private fun validateTasksExist(tasks: List<Task>, errorMessage: String) {
        if (tasks.isEmpty()) {
            throw TaskEditException(errorMessage)
        }
    }

    private fun writeTasksToFile(tasks: List<Task>) {
        csvWriter.writeToFile(tasks, TASK_FILE)
    }

    private fun findTaskIndexById(tasks: List<Task>, id: UUID): Int {
        return tasks.indexOfFirst { it.id == id }
    }


    private fun validateTaskExists(taskIndex: Int, id: UUID, errorMessage: String) {
        if (taskIndex == -1) {
            throw TaskEditException("Task with ID $id not found: $errorMessage")
        }
    }

    private fun addTaskToList(tasks: List<Task>, newTask: Task): List<Task> {
        return tasks.toMutableList().apply {
            add(newTask)
        }
    }

    private fun updateTaskInList(tasks: List<Task>, index: Int, updatedTask: Task): List<Task> {
        return tasks.toMutableList().apply {
            this[index] = updatedTask
        }
    }

    private fun removeTaskFromList(tasks: List<Task>, index: Int): List<Task> {
        return tasks.toMutableList().apply {
            removeAt(index)
        }
    }

    companion object {
        const val TASK_FILE = "tasks.csv"
    }
}