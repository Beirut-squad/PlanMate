package org.example.logic.use_cases.task_managemnt


import logic.exceptions.task_management_exception.BlankFieldsException
import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    suspend fun createTask(title: String, description: String, state: State, projectId : UUID, creatorUserID : UUID) {
        validateFields(title, description)
        val task = buildTask(title, description, state,projectId,creatorUserID)
        saveTaskAndLog(task)
    }

    private fun validateFields(title: String, description: String) {
        when {
            title.isBlank() -> throw BlankFieldsException("Title must not be blank")
            description.isBlank() -> throw BlankFieldsException("Description must not be blank")
        }
    }

    private fun buildTask(title: String, description: String, state: State,projectId : UUID,creatorUserID : UUID): Task {
        return Task(
            id = UUID.randomUUID(),
            projectId = projectId,
            title = title,
            description = description,
            state = state,
            creatorUserID = creatorUserID,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()

        )
    }
    private suspend fun saveTaskAndLog(task: Task) {
        taskRepository.createTask(task)
        createTaskLogUseCase.createTaskLog(task.creatorUserID, null, task)
    }
}