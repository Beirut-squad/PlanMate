package org.example.data.repositories.task_repository

import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.exceptions.TaskDeletionException
import org.example.logic.exceptions.TaskEditException
import org.example.logic.exceptions.GetAllTasksException
import org.example.logic.exceptions.GetTaskException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {
    override fun createTask(task: Task): Result<Unit> {
        return taskDataSource.createTask(task)
    }


    override fun editTask(task: Task): Result<Unit> {
        return taskDataSource.editTask(task)
    }

    override fun deleteTask(id: UUID): Result<Unit> {
        return taskDataSource.deleteTask(id)
    }

    override fun getAllTasks(): Result<List<Task>> {
        return taskDataSource.getAllTasks()
    }

    override fun getTask(id: UUID): Result<Task> {
        return taskDataSource.getTask(id)
    }
}