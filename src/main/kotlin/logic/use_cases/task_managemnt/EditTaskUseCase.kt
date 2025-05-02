package org.example.logic.use_cases.task_managemnt

import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.exceptions.NoFieldsToUpdateException
import org.example.logic.exceptions.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.time.LocalDateTime

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {

    fun editTask(task: Task, newTitle: String?, newDescription: String?, newState: String?) {
        val isTitleValid = newTitle.isNullOrBlank()
        val isDescriptionValid = newDescription.isNullOrBlank()
        val isStateValid = newState.isNullOrBlank()

        if (isTitleValid && isDescriptionValid && isStateValid) {
            throw NoFieldsToUpdateException("At least one non-blank field must be provided")
        } else {
            val updatedTask = task.copy(
                title = if (!isTitleValid) newTitle else task.title,
                description = if (!isDescriptionValid) newDescription else task.description,
                state = if (!isStateValid) task.state.copy(name = newState!!) else task.state,
                updatedAt = LocalDateTime.now()
            )
            val result = taskRepository.editTask(updatedTask)
            if (result.isFailure) {
                throw TaskEditException("Failed to edit task")
            }
            createTaskLogUseCase.createTaskLog(task, updatedTask, task.creatorUserID)
        }
    }

}


