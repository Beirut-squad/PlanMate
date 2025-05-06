package org.example.fake_datasource

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
}