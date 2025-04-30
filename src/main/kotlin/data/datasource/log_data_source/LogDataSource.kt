package org.example.data.datasource.log_data_source

import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.UUID

interface LogDataSource {
    fun getProjectLogs(id: UUID): Result<List<ProjectLog>>
    fun getTaskLogs(id: UUID): Result<List<TaskLog>>
    fun saveProjectLog(projectLog: ProjectLog): Result<Unit>
    fun saveTaskLog(taskLog: TaskLog): Result<Unit>
    fun getAllProjectLogs(): Result<List<ProjectLog>>
    fun getAllTaskLogs(): Result<List<TaskLog>>
}