package org.example.data.repositories.log_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

class LogRepositoryImpl(
    private val logDataSource: LogDataSource
) : LogRepository {
    override fun getProjectLogs(id: UUID): List<ProjectLog> {
        TODO("Not yet implemented")
    }

    override fun getTaskLogs(id: UUID): List<TaskLog> {
        TODO("Not yet implemented")
    }

    override fun saveProjectLog(projectLog: ProjectLog): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun saveTaskLog(taskLog: TaskLog): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getAllProjectLogs(): List<ProjectLog> {
        TODO("Not yet implemented")
    }

    override fun getAllTaskLogs(): List<TaskLog> {
        TODO("Not yet implemented")
    }
}