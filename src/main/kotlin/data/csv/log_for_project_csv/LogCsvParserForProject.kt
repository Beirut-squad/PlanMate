package org.example.data.csv.log_csv_parser

import org.example.data.datasource.log_data_source.LogDataSourceForProject
import org.example.models.ProjectLog
import java.io.File

class LogCsvParserForProject(private val file:File):LogDataSourceForProject {


    fun paserLogs(line :String):List<ProjectLog> {
        return emptyList()
    }

    override fun getHistory(): List<ProjectLog> {
        TODO("Not yet implemented")
    }

    override fun createLog(log: ProjectLog) {
        TODO("Not yet implemented")
    }

}