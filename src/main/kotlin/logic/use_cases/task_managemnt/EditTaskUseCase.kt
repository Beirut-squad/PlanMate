package org.example.logic.use_cases.task_managemnt

import logic.exceptions.task_management_exception.NoFieldsToUpdateException
import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    suspend fun editTask(task: Task, newTitle: String?, newDescription: String?, newState: State) {
        validateInputFields(newTitle, newDescription)

        val updatedTask = createUpdatedTask(task, newTitle, newDescription, newState)

        saveUpdatedTask(updatedTask)

        createTaskLogUseCase.createTaskLog(task.creatorUserID, task, updatedTask)
    }

    private fun validateInputFields(newTitle: String?, newDescription: String?) {
        val isTitleEmpty = newTitle.isNullOrBlank()
        val isDescriptionEmpty = newDescription.isNullOrBlank()

        if (isTitleEmpty && isDescriptionEmpty) {
            throw NoFieldsToUpdateException("At least one non-blank field must be provided")
        }
    }

    private fun createUpdatedTask(task: Task, newTitle: String?, newDescription: String?, newState: State): Task {
        return task.copy(
            title = newTitle?.takeIf { it.isNotBlank() } ?: task.title,
            description = newDescription?.takeIf { it.isNotBlank() } ?: task.description,
            state = newState,
            updatedAt = LocalDateTime.now()
        )
    }

    private suspend fun saveUpdatedTask(updatedTask: Task) {
        taskRepository.editTask(updatedTask)
    }
}