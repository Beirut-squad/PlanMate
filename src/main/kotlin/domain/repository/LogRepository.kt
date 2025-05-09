package org.example.domain.repository

import data.csv.model.ProjectLog
import domain.model.TaskLog
import java.util.*

interface LogRepository {
    fun getProjectLogs(projectId: UUID): List<ProjectLog>
    fun getTaskLogs(taskId: UUID): List<TaskLog>
    fun saveProjectLog(projectLog: ProjectLog)
    fun saveTaskLog(taskLog: TaskLog)
    fun getAllProjectLogs(): List<ProjectLog>
    fun getAllTaskLogs(): List<TaskLog>
}