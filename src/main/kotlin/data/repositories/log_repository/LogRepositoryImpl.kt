package org.example.data.repositories.log_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

class LogRepositoryImpl(
    private val logDataSource: LogDataSource
) : LogRepository {

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
