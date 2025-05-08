package org.example.logic.use_cases.task_managemnt

import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.*

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase
){
    suspend fun deleteTask(task: Task, id : UUID){
        taskRepository.deleteTask(id)
        createTaskLogUseCase.createTaskLog(task.creatorUserID,task,null)
    }
}