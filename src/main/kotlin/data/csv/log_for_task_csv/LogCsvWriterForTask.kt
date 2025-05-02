package org.example.data.csv.log_for_task_csv

import org.example.data.csv.CsvWriter
import org.example.models.TaskLog

class LogCsvWriterForTask : CsvWriter<TaskLog> {
    override fun writeToFile(items: List<TaskLog>, filePath: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    internal fun isValidTaskLog(taskLog: TaskLog): Boolean {
        return true
    }
}