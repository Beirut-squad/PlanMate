package org.example.data.datasource.log_data_source

import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.UUID

interface LogDataSource {
    fun getTaskLogs(id: UUID): List<TaskLog>
    fun saveProjectLog(projectLog: ProjectLog)
    fun saveTaskLog(taskLog: TaskLog)
    fun getAllProjectLogs(): List<ProjectLog>
    fun getAllTaskLogs(): List<TaskLog>
}