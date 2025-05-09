package org.example.data.fake_datasource

import data.datasource.log.LogDataSource
import domain.exception.log.NoProjectLogsFoundException
import domain.model.ProjectLog
import domain.model.TaskLog
import java.util.*

class LogFakeDataSource : LogDataSource {
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