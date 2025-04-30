package org.example.data.csv.log_csv_parser

import org.example.data.csv.CsvWriter
import org.example.models.ProjectLog
import java.io.File

class LogCsvWriterForProject: CsvWriter<ProjectLog> {
    override fun writeToFile(items: List<ProjectLog>, filePath: String) {
        TODO("Not yet implemented")
    }


}