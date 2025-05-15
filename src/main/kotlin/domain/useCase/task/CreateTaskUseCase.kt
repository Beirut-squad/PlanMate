package domain.useCase.task

import domain.model.TaskState
import domain.model.Task
import domain.useCase.log.CreateTaskLogUseCase
import ui.common.exception.EmptyTaskDescriptionException
import ui.common.exception.EmptyTaskTitleException
import domain.repository.TaskRepository
import java.util.*

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase,
) {
    suspend fun createTask(title: String, description: String, taskState: TaskState, projectId : UUID, creatorUserID : UUID) {
        validateFields(title, description)
        val task = Task(
            projectId = projectId,
            title = title,
            description = description,
            taskState = taskState,
            creatorUserID = creatorUserID
        )
        saveTaskAndLog(task)
    }

    private fun validateFields(title: String, description: String) {
        when {
            title.isBlank() -> throw EmptyTaskTitleException()
            description.isBlank() -> throw EmptyTaskDescriptionException()
        }
    }

    private suspend fun saveTaskAndLog(task: Task) {
        taskRepository.createTask(task)
        createTaskLogUseCase.createTaskLog(task.creatorUserID, null, task)
    }
}