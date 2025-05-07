package org.example.fake_datasource

import logic.exceptions.task_management_exception.GetTaskException
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.models.Task
import java.util.*

class TaskDataSourceFakeImpl : TaskDataSource {
    private val tasks = mutableListOf<Task>()

    override fun createTask(task: Task): Result<Unit> {
        return Result.success(Unit).also {
            tasks.add(task)
        }
    }

    override fun editTask(task: Task): Result<Unit> {
        return tasks.find { it.id == task.id }?.let {
            tasks.removeIf { it.id == task.id }.let {
                tasks.add(task)
                Result.success(Unit)
            }
        } ?: Result.failure(NoSuchElementException("Task with id ${task.id} not found"))

    }

    override fun deleteTask(id: UUID): Result<Unit> {
        return tasks.find { it.id == id }?.let {
            tasks.removeIf { it.id == id }.let {
                Result.success(Unit)
            }
        } ?: Result.failure(NoSuchElementException("Task with id $id not found"))
    }

    override fun getAllTasks(): Result<List<Task>> {
        return Result.success(tasks)
    }

    override fun getTask(id: UUID): Result<Task> {
        return tasks.find { it.id == id }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Task with id $id not found"))
    }

    override fun getTaskByStateIdAndProjectId(
        projectId: UUID,
        stateId: UUID
    ): Result<List<Task>> {
        val allTasksResult = getAllTasks()
        val tasks = allTasksResult.getOrNull() ?: emptyList()

        val filteredTasks = tasks.filter { it.projectId == projectId && it.state.id == stateId }
        return if (filteredTasks.isNotEmpty()) {
            Result.success(filteredTasks)
        } else {
            Result.failure(Exception("No tasks found for the given project and state."))
        }
    }

    override fun getAllTasksForProject(projectId: UUID): Result<List<Task>> {
        return try {
            val tasksForProject = tasks.filter { it.projectId == projectId }
            Result.success(tasksForProject)
        } catch (e: Exception) {
            Result.failure(GetTaskException("Failed to retrieve task: ${e.message}"))
        }
    }
}