package org.example.logic.repositories.log_repository

import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

interface LogRepository {
    suspend fun getProjectLogs(projectId: UUID): List<ProjectLog>
    suspend fun getTaskLogs(taskId: UUID): List<TaskLog>
    suspend fun saveProjectLog(projectLog: ProjectLog)
    suspend fun saveTaskLog(taskLog: TaskLog)
    suspend fun getAllProjectLogs(): List<ProjectLog>
    suspend fun getAllTaskLogs(): List<TaskLog>
}