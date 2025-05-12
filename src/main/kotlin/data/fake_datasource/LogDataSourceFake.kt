package data.fake_datasource

import org.example.core.domain.exception.NoProjectLogsFoundException
import domain.model.ProjectLog
import domain.model.TaskLog
import org.example.data.datasource.LogDataSource
import java.util.*

class LogDataSourceFake : LogDataSource {
    private val projectLogs = mutableListOf<ProjectLog>()
    private val taskLogs = mutableListOf<TaskLog>()

    override suspend fun getProjectLogs(id: UUID): List<ProjectLog> {
        return projectLogs.filter { it.entityId == id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override suspend fun getTaskLogs(id: UUID): List<TaskLog> {
        return taskLogs.filter { it.entityId == id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override suspend fun saveProjectLog(projectLog: ProjectLog) {
        projectLogs.add(projectLog)
    }

    override suspend fun saveTaskLog(taskLog: TaskLog) {
        taskLogs.add(taskLog)
    }

    override suspend fun getAllProjectLogs(): List<ProjectLog> {
        return projectLogs
    }

    override suspend fun getAllTaskLogs(): List<TaskLog> {
        return taskLogs
    }
}