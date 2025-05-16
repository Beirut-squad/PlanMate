package data.datasource

import domain.model.ProjectLog
import domain.model.TaskLog
import java.util.*

interface LogDataSource {
   suspend fun getProjectLogs(id: UUID): List<ProjectLog>
   suspend fun getTaskLogs(id: UUID): List<TaskLog>
   suspend fun saveProjectLog(projectLog: ProjectLog)
   suspend fun saveTaskLog(taskLog: TaskLog)
   suspend fun getAllProjectLogs(): List<ProjectLog>
   suspend fun getAllTaskLogs(): List<TaskLog>
}