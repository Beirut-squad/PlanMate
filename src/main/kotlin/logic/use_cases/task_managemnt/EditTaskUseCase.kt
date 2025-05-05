package org.example.logic.use_cases.task_managemnt

import logic.use_cases.log.CreateTaskLogUseCase
import logic.exceptions.task_management_exception.NoFieldsToUpdateException
import logic.exceptions.task_management_exception.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    fun editTask(task: Task, newTitle: String?, newDescription: String?, newState: State) {
        validateInputFields(newTitle, newDescription)

        val updatedTask = createUpdatedTask(task, newTitle, newDescription, newState)

        saveUpdatedTask(updatedTask)

        createTaskLogUseCase.createTaskLog(task.creatorUserID, task, updatedTask)
    }

    private fun validateInputFields(newTitle: String?, newDescription: String?) {
        val isTitleEmpty = newTitle.isNullOrBlank()
        val isDescriptionEmpty = newDescription.isNullOrBlank()

        if (isTitleEmpty && isDescriptionEmpty ) {
            throw NoFieldsToUpdateException("At least one non-blank field must be provided")
        }
    }

    private fun createUpdatedTask(task: Task, newTitle: String?, newDescription: String?, newState: State): Task {
        val isTitleEmpty = newTitle.isNullOrBlank()
        val isDescriptionEmpty = newDescription.isNullOrBlank()

        return task.copy(
            title = if (!isTitleEmpty) newTitle else task.title,
            description = if (!isDescriptionEmpty) newDescription else task.description,
            state =  newState,
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