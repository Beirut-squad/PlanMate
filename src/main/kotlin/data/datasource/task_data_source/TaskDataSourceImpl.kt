package org.example.data.datasource.task_data_source

import data.csv.FileName
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter

import logic.exceptions.task_management_exception.GetAllTasksException
import logic.exceptions.task_management_exception.GetTaskException
import logic.exceptions.task_management_exception.TaskCreationException
import logic.exceptions.task_management_exception.TaskDeletionException
import logic.exceptions.task_management_exception.TaskEditException
import org.example.models.Task
import java.util.UUID

class TaskDataSourceImpl(
    private val csvReader: CsvReader<Task>,
    private val csvWriter: CsvWriter<Task>,
    private val TASKS: String = FileName.TASKS
) : TaskDataSource {


    override fun createTask(task: Task): Result<Unit> {
        return try {
            val currentTasks = csvReader.read(TASKS)
            val updatedTasks = addTaskToList(currentTasks, task)
            csvWriter.writeToFile(updatedTasks, TASKS)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(TaskCreationException("Failed to create task: ${exception.message}"))
        }
    }

    override fun editTask(task: Task): Result<Unit> {
        return try {
            val currentTasks = csvReader.read(TASKS)
            validateTasksExist(currentTasks, "No tasks found to edit")

            val taskIndex = findTaskIndexById(currentTasks, task.id)
            validateTaskExists(taskIndex, task.id, "No tasks found to edit")

            val updatedTasks = updateTaskInList(currentTasks, taskIndex, task)
            csvWriter.writeToFile(updatedTasks, TASKS)

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(TaskEditException("Failed to edit task: ${exception.message}"))
        }
    }

    override fun deleteTask(id: UUID): Result<Unit> {
        return try {
            val currentTasks = csvReader.read(TASKS)
            validateTasksExist(currentTasks, "No tasks found to delete")

            val taskIndex = findTaskIndexById(currentTasks, id)
            validateTaskExists(taskIndex, id, "Cannot delete task")

            val updatedTasks = removeTaskFromList(currentTasks, taskIndex)
            csvWriter.writeToFile(updatedTasks, TASKS)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(TaskDeletionException("Failed to delete task: ${e.message}"))
        }
    }

    override fun getAllTasks(): Result<List<Task>> {
        return try {
            val tasks = csvReader.read(TASKS)
            validateTasksExist(tasks, "No tasks found")

            Result.success(tasks)

        } catch (e: Exception) {
            Result.failure(GetAllTasksException("Failed to read tasks: ${e.message}"))
        }
    }

    override fun getTask(id: UUID): Result<Task> {
        return try {
            val tasks = csvReader.read(TASKS)
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

    override fun getTaskByStateIdAndProjectId(projectId: UUID, stateId: UUID): Result<List<Task>> {
        val allTasksResult = getAllTasks()
        // if (allTasksResult.isSuccess) {
        val tasks = allTasksResult.getOrNull() ?: emptyList()

        val filteredTasks = tasks.filter { it.projectId == projectId && it.state.id == stateId }
        return if (filteredTasks.isNotEmpty()) {
            Result.success(filteredTasks)
        } else {
            Result.failure(Exception("No tasks found for the given project and state."))  // إذا لم تجد مهام
        }
    }

    override fun getAllTasksForProject(projectId: UUID): Result<List<Task>> {
        return try {
            val tasks = csvReader.read(TASKS)
            val tasksForProject = tasks.filter { it.projectId == projectId }
            Result.success(tasksForProject)
        } catch (e: Exception) {
            Result.failure(GetTaskException("Failed to retrieve task: ${e.message}"))
        }
    }


    private fun validateTasksExist(tasks: List<Task>, errorMessage: String) {
        if (tasks.isEmpty()) {
            throw TaskEditException(errorMessage)
        }
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


}