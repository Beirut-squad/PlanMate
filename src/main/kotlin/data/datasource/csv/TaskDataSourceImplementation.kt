package org.example.data.datasource.csv

import domain.model.Task
import org.example.data.csv.helper.FileName
import org.example.data.csv.reader.CsvReader
import org.example.data.csv.writer.CsvWriter
import org.example.data.datasource.TaskDataSource
import org.example.domain.exceptions.task_management_exception.FailedToReadTaskException
import org.example.domain.exceptions.task_management_exception.GetAllTasksException
import org.example.domain.exceptions.task_management_exception.GetTaskException
import org.example.domain.exceptions.task_management_exception.TaskCreationException
import org.example.domain.exceptions.task_management_exception.TaskDeletionException
import org.example.domain.exceptions.task_management_exception.TaskEditException
import java.util.UUID

class TaskDataSourceImplementation(
    private val csvReader: CsvReader<Task>,
    private val csvWriter: CsvWriter<Task>,
    private val taskFile: String = FileName.TASKS
) : TaskDataSource {


    override suspend fun createTask(task: Task) {
        val currentTasks = csvReader.read(taskFile)
        val updatedTasks = addTaskToList(currentTasks, task)
        if (updatedTasks.isNotEmpty())
            csvWriter.writeToFile(updatedTasks, taskFile)
        else
            throw TaskCreationException("Failed to create task")

    }

    override suspend fun editTask(task: Task) {
        val currentTasks = csvReader.read(taskFile)
        validateTasksExist(currentTasks, "No tasks found to edit")

        val taskIndex = findTaskIndexById(currentTasks, task.id)
        validateTaskExists(taskIndex, task.id, "No tasks found to edit")

        val updatedTasks = updateTaskInList(currentTasks, taskIndex, task)

        if (updatedTasks.isNotEmpty())
            csvWriter.writeToFile(updatedTasks, taskFile)
        else
            throw TaskEditException("Failed to edit task")
    }

    override suspend fun deleteTask(id: UUID) {
        val currentTasks = csvReader.read(taskFile)
        validateTasksExist(currentTasks, "No tasks found to delete")

        val taskIndex = findTaskIndexById(currentTasks, id)
        validateTaskExists(taskIndex, id, "Cannot delete task")

        val updatedTasks = removeTaskFromList(currentTasks, taskIndex)
        if (updatedTasks.isNotEmpty())
            csvWriter.writeToFile(updatedTasks, taskFile)
        else
            throw TaskDeletionException("Failed to delete task")
    }

    override suspend fun getAllTasks(): List<Task> {
        val tasks = csvReader.read(taskFile)
        if (tasks.isEmpty()) {
            validateTasksExist(tasks, "No tasks found")
            throw GetAllTasksException("Failed to read tasks: ")
        } else {
            return tasks
        }
    }

    override suspend fun getTask(id: UUID): Task {
        val tasks = csvReader.read(taskFile)
        if (tasks.isEmpty())
            throw GetTaskException("Failed to retrieve task")
        else {
            val task = tasks.find { it.id == id }
            if (task == null) {
                throw GetTaskException("Task with ID $id not found")
            } else {
                return task
            }
        }
    }

    override suspend fun getTaskByStateIdAndProjectId(projectId: UUID, stateId: UUID): List<Task> {
        val tasks = getAllTasks()

        val filteredTasks = tasks.filter { it.projectId == projectId && it.state.id == stateId }
        return filteredTasks.ifEmpty {
            throw Exception("No tasks found for the given project and state.")
        }
    }

    override suspend fun getAllTasksForProject(projectId: UUID): List<Task> {
            val tasks = csvReader.read(taskFile)
            val tasksForProject = tasks.filter { it.projectId == projectId }
            return tasksForProject.ifEmpty { throw GetTaskException("Failed to retrieve task") }
    }


    private fun validateTasksExist(tasks: List<Task>, errorMessage: String) {
        if (tasks.isEmpty()) {
            throw FailedToReadTaskException(errorMessage)
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