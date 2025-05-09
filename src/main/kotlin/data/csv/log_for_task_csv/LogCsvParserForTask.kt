package org.example.data.csv.log_for_task_csv

import CsvParser
import data.csv.log_for_project_csv.LogsColumnIndexForProject
import org.example.data.csv.smartCsvSplit
import data.csv.task_csv.TaskCsvParser
import org.example.models.TaskLog
import java.time.LocalDateTime
import java.util.*

class LogCsvParserForTask (private val taskCsvParser: TaskCsvParser):CsvParser<TaskLog> {

    override fun parseFile(csvLines: List<String>): List<TaskLog> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override fun parseLine(line: String): TaskLog? {
        var cleanedLine = line.replace(" ", "")

        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = smartCsvSplit(cleanedLine)

        if (taskCsvParser.parseLine(parts[LogColumnIndexForTask.PREVIOUS_ENTITY]) == null || taskCsvParser.parseLine(parts[LogsColumnIndexForProject.CURRENT_ENTITY]) == null)
            throw Exception("entity is missing")

        return TaskLog(
            id = UUID.fromString(parts[LogColumnIndexForTask.LOG_ID]),
            userId = UUID.fromString(parts[LogColumnIndexForTask.USER_ID]),
            entityId = UUID.fromString(parts[LogColumnIndexForTask.ENTITY_ID]),
            previousEntity = taskCsvParser.parseLine(parts[LogColumnIndexForTask.PREVIOUS_ENTITY]),
            currentEntity =  taskCsvParser.parseLine(parts[LogColumnIndexForTask.CURRENT_ENTITY]),
            createdAt = LocalDateTime.parse(parts[LogColumnIndexForTask.CREATED_AT])
        )
    }
}