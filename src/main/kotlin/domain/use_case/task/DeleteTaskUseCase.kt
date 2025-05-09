package domain.use_case.task

import org.example.data.model.Task
import domain.use_case.log.CreateTaskLogUseCase
import org.example.domain.repository.TaskRepository
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