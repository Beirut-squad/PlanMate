package org.example.data.csv.log_csv_parser

import CsvParser
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.models.Log
import java.io.File

class LogCsvParser(private val file:File):LogDataSource {

    override fun getHistory(): List<Log> {
        TODO("Not yet implemented")
    }

    override fun createLog(log: Log):Boolean {
        TODO("Not yet implemented")
    }

    fun paserLogs(line :String):List<Log> {
        return emptyList()
    }

}