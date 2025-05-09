package org.example.data.fake_datasource

import org.example.data.model.Task
import data.datasource.task.TaskDataSource
import org.example.domain.exceptions.task_management_exception.GetTaskException
import java.util.*

class TaskFakeDataSource : TaskDataSource {
    private val tasks = mutableListOf<Task>()

    override suspend fun createTask(task: Task) {
        tasks.add(task)
    }

    override suspend fun editTask(task: Task) {
        tasks.find { it.id == task.id }?.let {
            tasks.removeIf { it.id == task.id }.let {
                tasks.add(task)
            }
        } ?: throw NoSuchElementException("Task with id ${task.id} not found")

    }

    override suspend fun deleteTask(id: UUID) {
        return tasks.find { it.id == id }?.let {
            tasks.removeIf { it.id == id }.let {
            }
        } ?: throw NoSuchElementException("Task with id $id not found")
    }

    override suspend fun getAllTasks(): List<Task> {
        return tasks
    }

    override suspend fun getTask(id: UUID): Task {
        return tasks.find { it.id == id }
            ?: throw NoSuchElementException("Task with id $id not found")
    }

    override suspend fun getTaskByStateIdAndProjectId(
        projectId: UUID,
        stateId: UUID
    ): List<Task> {
        val tasks = getAllTasks()

        val filteredTasks = tasks.filter { it.projectId == projectId && it.state.id == stateId }
        return filteredTasks.ifEmpty { throw Exception("No tasks found for the given project and state.") }

    }

    override suspend fun getAllTasksForProject(projectId: UUID): List<Task> {
        return tasks.filter { it.projectId == projectId }.ifEmpty { throw GetTaskException("No tasks found for this project.") }
    }
}
