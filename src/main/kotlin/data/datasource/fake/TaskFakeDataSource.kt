package data.datasource.fake

import domain.exception.StateNotFoundException
import domain.exception.TaskNotFoundException
import data.datasource.interfaces.TaskDataSource
import domain.model.Task
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
        } ?: throw TaskNotFoundException()

    }

    override suspend fun deleteTask(id: UUID) {
        return tasks.find { it.id == id }?.let {
            tasks.removeIf { it.id == id }.let {
            }
        } ?: throw TaskNotFoundException()
    }

    override suspend fun getAllTasks(): List<Task> {
        return tasks
    }

    override suspend fun getTask(id: UUID): Task {
        return tasks.find { it.id == id }
            ?: throw TaskNotFoundException()
    }

    override suspend fun getTasksByStateAndProjectIds(
        projectId: UUID,
        stateId: UUID
    ): List<Task> {
        val tasks = getAllTasks()

        val filteredTasks = tasks.filter { it.projectId == projectId && it.state.id == stateId }
        return filteredTasks.ifEmpty { throw StateNotFoundException() }

    }

    override suspend fun getAllTasksForProject(projectId: UUID): List<Task> {
        return tasks.filter { it.projectId == projectId }.ifEmpty { throw TaskNotFoundException() }
    }
}
