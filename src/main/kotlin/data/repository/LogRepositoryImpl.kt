package org.example.data.repository

import org.example.data.model.ProjectLog
import data.datasource.log.LogDataSource
import domain.model.TaskLog
import org.example.domain.repository.LogRepository
import java.util.*

class LogRepositoryImpl(
    private val logDataSource: LogDataSource
) : LogRepository {
    override fun getProjectLogs(projectId: UUID): List<ProjectLog> {
        return logDataSource.getProjectLogs(projectId)
    }

    override fun getTaskLogs(taskId: UUID): List<TaskLog> {
        return logDataSource.getTaskLogs(taskId)
    }

    override fun saveProjectLog(projectLog: ProjectLog) {
        logDataSource.saveProjectLog(projectLog)
    }

    override fun saveTaskLog(taskLog: TaskLog) {
        logDataSource.saveTaskLog(taskLog)
    }

    override fun getAllProjectLogs(): List<ProjectLog> {
        return logDataSource.getAllProjectLogs()
    }

    override fun getAllTaskLogs(): List<TaskLog> {
        return logDataSource.getAllTaskLogs()
    }
}
