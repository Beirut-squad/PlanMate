package org.example.data.csv.task_csv_parser

import CsvParser
import data.csv.task_csv.TaskColumnIndex
import org.example.data.csv.smartCsvSplit
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

class TaskCsvParser(
    private val stateCsvParser: CsvParser<State>
) : CsvParser<Task> {

    override fun parseFile(csvLines: List<String>): List<Task> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override fun parseLine(line: String): Task? {
        var cleanedLine = line.replace(" ", "")

        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = smartCsvSplit(cleanedLine)

        if (stateCsvParser.parseLine(parts[TaskColumnIndex.STATE]) == null) {
            throw Exception("state of the task is unavailable")
        }

        return Task(
            id = UUID.fromString(parts[TaskColumnIndex.TASK_ID]),
            projectId = UUID.fromString(parts[TaskColumnIndex.PROJECT_ID]),
            title = parts[TaskColumnIndex.TITLE],
            description = parts[TaskColumnIndex.DESCRIPTION],
            state = stateCsvParser.parseLine(parts[TaskColumnIndex.STATE])!!,
            creatorUserID = UUID.fromString(parts[TaskColumnIndex.CREATOR_USER_ID]),
            createdAt = LocalDateTime.parse(parts[TaskColumnIndex.CREATED_AT]),
            updatedAt = LocalDateTime.parse(parts[TaskColumnIndex.UPDATED_AT])
        )
    }


}
