package org.example.data.csv.task_csv_parser

import CsvParser
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.models.State
import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

class TaskCsvParser(
    private val stateCsvParser: CsvParser<State>
): CsvParser<Task> {

    override fun parseFile(csvLines: List<String>): List<Task> {
        if (csvLines.isEmpty())
            return emptyList()
        return csvLines.mapNotNull { parseLine(it) }
    }

    override fun parseLine(line: String): Task? {
        var cleanedLine = line.replace(" ", "")

        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = smartCsvSplit(cleanedLine)

        if (stateCsvParser.parseLine(parts[4]) == null){
            throw Exception("state of the task is unavailable")
        }

        // TODO : REFACTORING
        return Task(
            id = UUID.fromString(parts[0]),
            projectId = parts[1],
            title = parts[2],
            description = parts[3],
            state = stateCsvParser.parseLine(parts[4])!!,
            creatorUserID = UUID.fromString(parts[5]),
            createdAt = LocalDateTime.parse(parts[6]),
            updatedAt = LocalDateTime.parse(parts[7])
        )
    }

    private fun smartCsvSplit(line: String): List<String> {
        val fields = mutableListOf<String>()
        var current = StringBuilder()
        var bracketDepth = 0

        for (char in line) {
            when (char) {
                '[' -> {
                    bracketDepth++
                    current.append(char)
                }
                ']' -> {
                    bracketDepth--
                    current.append(char)
                }
                ',' -> {
                    if (bracketDepth == 0) {
                        fields.add(current.toString().trim())
                        current = StringBuilder()
                    } else {
                        current.append(char)
                    }
                }
                else -> current.append(char)
            }
        }
        fields.add(current.toString().trim()) // add last field
        return fields
    }

}
