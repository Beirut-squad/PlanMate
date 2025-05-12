package domain.use_case.task

import domain.model.State
import domain.model.Task
import domain.use_case.log.CreateTaskLogUseCase
import domain.exception.EmptyTaskDescriptionException
import domain.exception.EmptyTaskTitleException
import domain.repository.TaskRepository
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
            title.isBlank() -> throw EmptyTaskTitleException()
            description.isBlank() -> throw EmptyTaskDescriptionException()
        }
    }

    private fun buildTask(title: String, description: String, state: State, projectId : UUID, creatorUserID : UUID): Task {
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