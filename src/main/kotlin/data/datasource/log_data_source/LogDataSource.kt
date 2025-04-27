package org.example.data.datasource.log_data_source

import org.example.models.Log

interface LogDataSource {
    fun showHistory(): List<Log>
    fun createLog(log: Log)
}