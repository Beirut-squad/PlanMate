package org.example.logic.use_cases.task_managemnt

import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.exceptions.task_managment_exception.TaskDeletionException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.UUID

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createTaskLogUseCase: CreateTaskLogUseCase
){
    fun deleteTask(task: Task,id : UUID){
        val result = taskRepository.deleteTask(id)
        if (result.isFailure) {
            throw TaskDeletionException("Failed to delete task")
        }
        createTaskLogUseCase.createTaskLog(task.creatorUserID,task,null)
    }
}