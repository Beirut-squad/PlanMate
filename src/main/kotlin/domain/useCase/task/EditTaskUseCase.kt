package domain.useCase.task

import ui.common.exception.EmptyFieldException
import domain.model.Task
import domain.model.TaskState
import domain.useCase.log.CreateTaskLogUseCase

import domain.repository.TaskRepository
import java.time.LocalDateTime
import java.util.*

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    suspend fun editTask(task: Task, newTitle: String?, newDescription: String?, newTaskState: TaskState, editorUserId: UUID) {
        validateInputFields(newTitle, newDescription,newTaskState,task)

        val updatedTask = createUpdatedTask(task, newTitle, newDescription, newTaskState)

        taskRepository.editTask(updatedTask)

        createTaskLogUseCase.createTaskLog(editorUserId, task, updatedTask)
    }

    private fun validateInputFields(newTitle: String?, newDescription: String?, newTaskState: TaskState, task : Task) {
            if (newTaskState == task.taskState && newTitle.isNullOrBlank() && newDescription.isNullOrBlank()){
                 throw EmptyFieldException()
        }
    }

    private fun createUpdatedTask(task: Task, newTitle: String?, newDescription: String?, newTaskState: TaskState): Task {
        return task.copy(
            title = newTitle?.takeIf { it.isNotBlank() } ?: task.title,
            description = newDescription?.takeIf { it.isNotBlank() } ?: task.description,
            taskState = newTaskState,
            updatedAt = LocalDateTime.now()
        )
    }
}