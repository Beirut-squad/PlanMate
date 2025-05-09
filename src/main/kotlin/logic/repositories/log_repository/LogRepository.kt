package org.example.logic.repositories.log_repository

import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

interface LogRepository {
    fun getTaskLogs(taskId: UUID): List<TaskLog>
    fun saveProjectLog(projectLog: ProjectLog)
    fun saveTaskLog(taskLog: TaskLog)
    fun getAllProjectLogs(): List<ProjectLog>
    fun getAllTaskLogs(): List<TaskLog>
}