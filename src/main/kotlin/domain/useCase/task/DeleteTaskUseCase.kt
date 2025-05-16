package domain.useCase.task

import domain.model.Task
import domain.useCase.log.CreateTaskLogUseCase
import domain.repository.TaskRepository
import java.util.*

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase
){
    suspend fun deleteTask(task: Task, userID : UUID){
        taskRepository.deleteTask(task.id)
        createTaskLogUseCase.createTaskLog(userID,task,null)
    }
}