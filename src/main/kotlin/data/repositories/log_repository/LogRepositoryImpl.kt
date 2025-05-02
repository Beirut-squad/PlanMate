package org.example.data.repositories.log_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

class LogRepositoryImpl(
    private val logDataSource: LogDataSource
) : LogRepository {
    override fun getProjectLogs(projectId: UUID): Result<List<ProjectLog>> {
        return logDataSource.getProjectLogs(projectId)
    }

    override fun getTaskLogs(taskId: UUID): Result<List<TaskLog>> {
        return logDataSource.getTaskLogs(taskId)
    }

    override fun saveProjectLog(projectLog: ProjectLog): Result<Unit> {
        return logDataSource.saveProjectLog(projectLog)
    }

    override fun saveTaskLog(taskLog: TaskLog): Result<Unit> {
        return logDataSource.saveTaskLog(taskLog)
    }

    override fun getAllProjectLogs(): Result<List<ProjectLog>> {
        return logDataSource.getAllProjectLogs()
    }

    override fun getAllTaskLogs(): Result<List<TaskLog>> {
        return logDataSource.getAllTaskLogs()
    }
}
