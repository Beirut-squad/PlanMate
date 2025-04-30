package org.example.data.repositories.log_repository

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.*

class LogRepositoryImpl : LogRepository {
    override fun getProjectHistory(id: UUID): List<ProjectLog> {
        TODO("Not yet implemented")
    }

    override fun getTaskHistory(id: UUID): List<TaskLog> {
        TODO("Not yet implemented")
    }

    override fun createTaskLog(taskLog: TaskLog) {
        TODO("Not yet implemented")
    }

    override fun createProjectLog(projectLog: ProjectLog) {
        TODO("Not yet implemented")
    }


}