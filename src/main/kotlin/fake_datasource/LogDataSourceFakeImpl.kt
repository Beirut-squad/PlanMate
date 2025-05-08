package org.example.fake_datasource

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

class LogDataSourceFakeImpl: LogDataSource {
    private val projectLogs = mutableListOf<ProjectLog>()
    private val taskLogs = mutableListOf<TaskLog>()

    override fun getProjectLogs(id: UUID): Result<List<ProjectLog>> {
        return Result.success(
            projectLogs.filter { it.entityId == id }
        )
    }

    override fun getTaskLogs(id: UUID): Result<List<TaskLog>> {
        return Result.success(
            taskLogs.filter { it.entityId == id }
        )
    }

    override fun saveProjectLog(projectLog: ProjectLog): Result<Unit> {
        projectLogs.add(projectLog)
        return Result.success(Unit)
    }

    override fun saveTaskLog(taskLog: TaskLog): Result<Unit> {
        taskLogs.add(taskLog)
        return Result.success(Unit)
    }

    override fun getAllProjectLogs(): Result<List<ProjectLog>> {
        return Result.success(projectLogs)
    }

    override fun getAllTaskLogs(): Result<List<TaskLog>> {
        return Result.success(taskLogs)
    }
}