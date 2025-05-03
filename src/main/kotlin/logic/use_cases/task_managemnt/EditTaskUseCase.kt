package org.example.logic.use_cases.task_managemnt

import logic.use_cases.log.CreateTaskLogUseCase
import logic.exceptions.task_management_exception.NoFieldsToUpdateException
import logic.exceptions.task_management_exception.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.time.LocalDateTime

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    fun editTask(task: Task, newTitle: String?, newDescription: String?, newState: String?) {
        validateInputFields(newTitle, newDescription, newState)

        val updatedTask = createUpdatedTask(task, newTitle, newDescription, newState)

        saveUpdatedTask(updatedTask)

        createTaskLogUseCase.createTaskLog(task.creatorUserID, task, updatedTask)
    }

    private fun validateInputFields(newTitle: String?, newDescription: String?, newState: String?) {
        val isTitleEmpty = newTitle.isNullOrBlank()
        val isDescriptionEmpty = newDescription.isNullOrBlank()
        val isStateEmpty = newState.isNullOrBlank()

        if (isTitleEmpty && isDescriptionEmpty && isStateEmpty) {
            throw NoFieldsToUpdateException("At least one non-blank field must be provided")
        }
    }

    private fun createUpdatedTask(task: Task, newTitle: String?, newDescription: String?, newState: String?): Task {
        val isTitleEmpty = newTitle.isNullOrBlank()
        val isDescriptionEmpty = newDescription.isNullOrBlank()
        val isStateEmpty = newState.isNullOrBlank()

        return task.copy(
            title = if (!isTitleEmpty) newTitle else task.title,
            description = if (!isDescriptionEmpty) newDescription else task.description,
            taskState = if (!isStateEmpty) task.taskState.copy(name = newState) else task.taskState,
            updatedAt = LocalDateTime.now()
        )
    }

    private fun saveUpdatedTask(updatedTask: Task) {
        val result = taskRepository.editTask(updatedTask)
        if (result.isFailure) {
            throw TaskEditException("Failed to edit task")
        }
    }


}


