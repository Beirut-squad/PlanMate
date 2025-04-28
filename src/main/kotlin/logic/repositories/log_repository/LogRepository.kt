package org.example.logic.repositories.log_repository

import org.example.models.Log
import java.util.UUID

interface LogRepository {
    fun getProjectHistory(id:UUID): List<Log>
    fun getTaskHistory(id:UUID): List<Log>
    fun createLog(log: Log)
}