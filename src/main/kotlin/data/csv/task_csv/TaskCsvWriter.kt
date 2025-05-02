package org.example.data.csv.task_csv_parser

import org.example.data.csv.CsvWriter
import org.example.models.Task

class TaskCsvWriter : CsvWriter<Task> {
    override fun writeToFile(items: List<Task>, filePath: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}