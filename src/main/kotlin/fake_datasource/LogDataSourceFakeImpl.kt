package org.example.fake_datasource

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.logic.exceptions.log_exceptions.NoProjectLogsFoundException
import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

class LogDataSourceFakeImpl : LogDataSource {
    private val projectLogs = mutableListOf<ProjectLog>()
    private val taskLogs = mutableListOf<TaskLog>()

    override fun getProjectLogs(id: UUID): List<ProjectLog> {
        return projectLogs.filter { it.entityId == id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override fun getTaskLogs(id: UUID): List<TaskLog> {
        return taskLogs.filter { it.entityId == id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override fun saveProjectLog(projectLog: ProjectLog) {
        projectLogs.add(projectLog)
    }

    override fun saveTaskLog(taskLog: TaskLog) {
        taskLogs.add(taskLog)
    }

    override fun getAllProjectLogs(): List<ProjectLog> {
        return projectLogs
    }

    override fun getAllTaskLogs(): List<TaskLog> {
        return taskLogs
    }
}