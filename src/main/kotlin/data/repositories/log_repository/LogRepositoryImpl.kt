package org.example.data.repositories.log_repository

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.Log
import java.util.*

class LogRepositoryImpl : LogRepository {
    override fun getProjectHistory(id: UUID): List<Log> {
        TODO("Not yet implemented")
    }

    override fun getTaskHistory(id: UUID): List<Log> {
        TODO("Not yet implemented")
    }

    override fun createLog(log: Log) {
        TODO("Not yet implemented")
    }

}