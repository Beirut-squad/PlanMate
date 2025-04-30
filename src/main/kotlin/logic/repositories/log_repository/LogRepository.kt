package org.example.logic.repositories.log_repository

import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

interface LogRepository {
    fun getProjectLogs(id: UUID): List<ProjectLog>
    fun getTaskLogs(id: UUID): List<TaskLog>
    fun saveProjectLog(projectLog: ProjectLog): Result<Unit>
    fun saveTaskLog(taskLog: TaskLog): Result<Unit>
    fun getAllProjectLogs(): List<ProjectLog>
    fun getAllTaskLogs(): List<TaskLog>
}