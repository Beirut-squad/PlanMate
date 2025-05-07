package org.example.data.datasource.log_data_source

import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.UUID

interface LogDataSource {
   suspend fun getProjectLogs(id: UUID): List<ProjectLog>
   suspend fun getTaskLogs(id: UUID): List<TaskLog>
   suspend fun saveProjectLog(projectLog: ProjectLog)
   suspend fun saveTaskLog(taskLog: TaskLog)
   suspend fun getAllProjectLogs(): List<ProjectLog>
   suspend fun getAllTaskLogs(): List<TaskLog>
}