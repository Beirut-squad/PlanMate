package domain.repository

import domain.model.ProjectLog
import domain.model.TaskLog
import java.util.*

interface LogRepository {
    suspend fun getProjectLogs(projectId: UUID): List<ProjectLog>
    suspend fun getTaskLogs(taskId: UUID): List<TaskLog>
    suspend fun saveProjectLog(projectLog: ProjectLog)
    suspend fun saveTaskLog(taskLog: TaskLog)
    suspend fun getAllProjectLogs(): List<ProjectLog>
    suspend fun getAllTaskLogs(): List<TaskLog>
}