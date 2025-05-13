package domain.use_case.task

import ui.common.exception.EmptyFieldException
import domain.model.Task
import domain.model.State
import domain.use_case.log.CreateTaskLogUseCase

import domain.repository.TaskRepository
import java.time.LocalDateTime
import java.util.*

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    suspend fun editTask(task: Task, newTitle: String?, newDescription: String?, newState: State, editorUserId: UUID) {
        validateInputFields(newTitle, newDescription,newState,task)

        val updatedTask = createUpdatedTask(task, newTitle, newDescription, newState)

        saveUpdatedTask(updatedTask)

        createTaskLogUseCase.createTaskLog(editorUserId, task, updatedTask)
    }

    private fun validateInputFields(newTitle: String?, newDescription: String?,newState: State,task : Task) {
            if (newState == task.state && newTitle.isNullOrBlank() && newDescription.isNullOrBlank()){
                 throw EmptyFieldException()
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