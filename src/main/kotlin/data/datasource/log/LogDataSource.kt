package data.datasource.log

import domain.model.ProjectLog
import domain.model.TaskLog
import java.util.UUID

interface LogDataSource {
    fun getProjectLogs(id: UUID): List<ProjectLog>
    fun getTaskLogs(id: UUID): List<TaskLog>
    fun saveProjectLog(projectLog: ProjectLog)
    fun saveTaskLog(taskLog: TaskLog)
    fun getAllProjectLogs(): List<ProjectLog>
    fun getAllTaskLogs(): List<TaskLog>
}