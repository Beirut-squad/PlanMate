package org.example.data.csv.parser

import org.example.core.domain.exception.CsvValidationException
import org.example.data.csv.column_index.TaskLogColumnIndex
import org.example.data.csv.helper.smartCsvSplit
import domain.model.TaskLog
import java.time.LocalDateTime
import java.util.*

class TaskLogParser (private val taskParser: TaskParser): CsvParser<TaskLog> {



    override suspend fun parseFile(csvLines: List<String>): List<TaskLog> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override suspend fun parseLine(line: String): TaskLog? {
        var cleanedLine = line.replace(" ", "")

        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = smartCsvSplit(cleanedLine)

        if (taskParser.parseLine(parts[TaskLogColumnIndex.PREVIOUS_ENTITY]) == null || taskParser.parseLine(parts[TaskLogColumnIndex.CURRENT_ENTITY]) == null)
            throw CsvValidationException()

        return TaskLog(
            id = UUID.fromString(parts[TaskLogColumnIndex.ID]),
            userId = UUID.fromString(parts[TaskLogColumnIndex.USER_ID]),
            entityId = UUID.fromString(parts[TaskLogColumnIndex.ENTITY_ID]),
            previousEntity = taskParser.parseLine(parts[TaskLogColumnIndex.PREVIOUS_ENTITY]),
            currentEntity =  taskParser.parseLine(parts[TaskLogColumnIndex.CURRENT_ENTITY]),
            createdAt = LocalDateTime.parse(parts[TaskLogColumnIndex.CREATED_AT])
        )
    }
}