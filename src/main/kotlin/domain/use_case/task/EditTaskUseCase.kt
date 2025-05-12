package domain.use_case.task

import domain.model.Task
import domain.model.State
import domain.use_case.log.CreateTaskLogUseCase
import org.example.core.domain.exception.EmptyTaskDescriptionException
import org.example.core.domain.exception.EmptyTaskTitleException
import org.example.domain.repository.TaskRepository
import java.time.LocalDateTime
import java.util.*

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    suspend fun editTask(task: Task, newTitle: String?, newDescription: String?, newState: State, editorUserId: UUID) {
        validateInputFields(newTitle, newDescription)

        val updatedTask = createUpdatedTask(task, newTitle, newDescription, newState)

        saveUpdatedTask(updatedTask)

        createTaskLogUseCase.createTaskLog(editorUserId, task, updatedTask)
    }

    private fun validateInputFields(newTitle: String?, newDescription: String?) {
        when {
            newTitle.isNullOrBlank() -> throw EmptyTaskTitleException()
            newDescription.isNullOrBlank() -> throw EmptyTaskDescriptionException()
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