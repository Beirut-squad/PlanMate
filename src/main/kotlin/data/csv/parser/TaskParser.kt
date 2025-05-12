package org.example.data.csv.parser

import data.exception.StateNotFoundException
import domain.model.State
import domain.model.Task
import org.example.data.csv.column_index.TaskColumnIndex
import org.example.data.csv.helper.smartCsvSplit
import java.time.LocalDateTime
import java.util.*

class TaskParser(
    private val stateCsvParser: CsvParser<State>
) : CsvParser<Task> {

    override suspend fun parseFile(csvLines: List<String>): List<Task> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override suspend fun parseLine(line: String): Task? {
        var cleanedLine = line.replace(" ", "")

        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = smartCsvSplit(cleanedLine)

        if (stateCsvParser.parseLine(parts[TaskColumnIndex.STATE]) == null) {
            throw StateNotFoundException()
        }

        return Task(
            id = UUID.fromString(parts[TaskColumnIndex.ID]),
            projectId = UUID.fromString(parts[TaskColumnIndex.PROJECT_ID]),
            title = parts[TaskColumnIndex.NAME],
            description = parts[TaskColumnIndex.DESCRIPTION],
            state = stateCsvParser.parseLine(parts[TaskColumnIndex.STATE])!!,
            creatorUserID = UUID.fromString(parts[TaskColumnIndex.CREATOR_USER_ID]),
            createdAt = LocalDateTime.parse(parts[TaskColumnIndex.CREATED_AT]),
            updatedAt = LocalDateTime.parse(parts[TaskColumnIndex.UPDATED_AT])
        )
    }
}
