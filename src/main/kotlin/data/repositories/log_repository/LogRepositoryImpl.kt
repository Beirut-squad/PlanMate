package org.example.data.repositories.log_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import org.example.models.TaskLog
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
