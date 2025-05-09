package domain.use_case.task

import data.csv.model.State
import data.csv.model.Task
import domain.use_case.log.CreateTaskLogUseCase
import org.example.domain.exceptions.task_management_exception.NoFieldsToUpdateException
import org.example.domain.repository.TaskRepository
import java.time.LocalDateTime
import java.util.*

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    suspend fun editTask(task: Task, newTitle: String?, newDescription: String?, newState: State, editorUserId : UUID) {
        validateInputFields(newTitle, newDescription)

        val updatedTask = createUpdatedTask(task, newTitle, newDescription, newState)

        saveUpdatedTask(updatedTask)

        createTaskLogUseCase.createTaskLog(editorUserId, task, updatedTask)
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
            name = newTitle?.takeIf { it.isNotBlank() } ?: task.name,
            description = newDescription?.takeIf { it.isNotBlank() } ?: task.description,
            state = newState,
            updatedAt = LocalDateTime.now()
        )
    }

    private suspend fun saveUpdatedTask(updatedTask: Task) {
        taskRepository.editTask(updatedTask)
    }
}