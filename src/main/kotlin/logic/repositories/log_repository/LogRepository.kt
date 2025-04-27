package org.example.logic.repositories.log_repository

import org.example.models.Log

interface LogRepository {
    fun showProjectHistory(): List<Log>
    fun showTaskHistory(): List<Log>
    fun createLog(log: Log)
}