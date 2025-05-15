package data.repository

import data.datasource.LogDataSource
import domain.model.ProjectLog
import domain.model.TaskLog
import domain.repository.LogRepository
import java.util.*

class LogRepositoryImpl(
    private val logDataSource: LogDataSource
) : LogRepository {
    override suspend fun getProjectLogs(projectId: UUID): List<ProjectLog> {
        return logDataSource.getProjectLogs(projectId)
    }

    override suspend fun getTaskLogs(taskId: UUID): List<TaskLog> {
        return logDataSource.getTaskLogs(taskId)
    }

    override suspend fun saveProjectLog(projectLog: ProjectLog) {
        logDataSource.saveProjectLog(projectLog)
    }

    override suspend fun saveTaskLog(taskLog: TaskLog) {
        logDataSource.saveTaskLog(taskLog)
    }

    override suspend fun getAllProjectLogs(): List<ProjectLog> {
        return logDataSource.getAllProjectLogs()
    }

    override suspend fun getAllTaskLogs(): List<TaskLog> {
        return logDataSource.getAllTaskLogs()
    }
}
