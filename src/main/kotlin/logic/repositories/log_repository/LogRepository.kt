package org.example.logic.repositories.log_repository

import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

interface LogRepository {
    fun getProjectHistory(id: UUID): List<ProjectLog>
    fun getTaskHistory(id: UUID): List<TaskLog>
    fun createTaskLog(taskLog: TaskLog)
    fun createProjectLog(projectLog: ProjectLog)
}