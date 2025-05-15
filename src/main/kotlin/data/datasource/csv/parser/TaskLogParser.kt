package data.datasource.csv.parser

import ui.common.exception.CsvValidationException
import domain.model.TaskLog
import java.time.LocalDateTime
import java.util.*

class TaskLogParser (
    private val taskParser: TaskParser
): CsvParser<TaskLog> {

    override suspend fun parseFile(csvLines: List<String>)
    : List<TaskLog> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override suspend fun parseLine(line: String)
    : TaskLog? {
        var cleanedLine = line.replace(" ", "")

        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = data.datasource.csv.helper.smartCsvSplit(cleanedLine)

        if (taskParser.parseLine(parts[data.datasource.csv.column_index.TaskLogColumnIndex.PREVIOUS_ENTITY]) == null || taskParser.parseLine(parts[data.datasource.csv.column_index.TaskLogColumnIndex.CURRENT_ENTITY]) == null)
            throw CsvValidationException()

        return TaskLog(
            id = UUID.fromString(parts[data.datasource.csv.column_index.TaskLogColumnIndex.ID]),
            userId = UUID.fromString(parts[data.datasource.csv.column_index.TaskLogColumnIndex.USER_ID]),
            entityId = UUID.fromString(parts[data.datasource.csv.column_index.TaskLogColumnIndex.ENTITY_ID]),
            previousEntity = taskParser.parseLine(parts[data.datasource.csv.column_index.TaskLogColumnIndex.PREVIOUS_ENTITY]),
            currentEntity =  taskParser.parseLine(parts[data.datasource.csv.column_index.TaskLogColumnIndex.CURRENT_ENTITY]),
            createdAt = LocalDateTime.parse(parts[data.datasource.csv.column_index.TaskLogColumnIndex.CREATED_AT])
        )
    }
}