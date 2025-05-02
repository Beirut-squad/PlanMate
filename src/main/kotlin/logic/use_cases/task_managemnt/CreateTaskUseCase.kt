package org.example.logic.use_cases.task_managemnt


import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.exceptions.task_managment_exception.BlankFieldsException
import org.example.logic.exceptions.task_managment_exception.TaskCreationException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.UUID

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    fun createTask(title: String, description: String, stateName: String) {
        validateFields(title, description, stateName)
        val task = buildTask(title, description, stateName)
        saveTaskAndLog(task)
    }

    private fun validateFields(title: String, description: String, stateName: String) {
        when {
            title.isBlank() -> throw BlankFieldsException("Title must not be blank")
            description.isBlank() -> throw BlankFieldsException("Description must not be blank")
            stateName.isBlank() -> throw BlankFieldsException("State name must not be blank")
        }
    }

    private fun buildTask(title: String, description: String, stateName: String): Task {
        return Task(
            id = UUID.randomUUID(),
            projectId = UUID.randomUUID(),
            title = title,
            description = description,
            state = State(id = UUID.randomUUID(), name = stateName),
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()

        )
    }
    private fun saveTaskAndLog(task: Task) {
        val result = taskRepository.createTask(task)
        if (result.isFailure) {
            throw TaskCreationException("Failed to create task")
        }
        createTaskLogUseCase.createTaskLog(task.creatorUserID, null, task)
    }
}