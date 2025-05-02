package data.csv.log_for_project_csv

import CsvParser
import data.csv.project_csv.ProjectCsvParser
import org.example.models.ProjectLog
import java.time.LocalDateTime
import java.util.*

class LogCsvParserForProject(private val projectCsvParser: ProjectCsvParser):CsvParser<ProjectLog> {



    override fun parseFile(csvLines: List<String>): List<ProjectLog> {
        if (csvLines.isEmpty())
            return emptyList()
        return csvLines.mapNotNull { parseLine(it) }
    }

    override fun parseLine(line: String): ProjectLog? {
        var cleanedLine = line.replace(" ", "")

        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = smartCsvSplit(cleanedLine)

            if (projectCsvParser.parseLine(parts[LogsColumnIndexForProject.PREVIOUS_ENTITY]) == null || projectCsvParser.parseLine(parts[LogsColumnIndexForProject.CURRENT_ENTITY]) == null)
            throw Exception("entity is missing")

        return ProjectLog(
            id = UUID.fromString(parts[LogsColumnIndexForProject.LOG_ID]),
            userId = UUID.fromString(parts[LogsColumnIndexForProject.USER_ID]),
            entityId = UUID.fromString(parts[LogsColumnIndexForProject.ENTITY_ID]),
            previousEntity = projectCsvParser.parseLine(parts[LogsColumnIndexForProject.PREVIOUS_ENTITY]),
            currentEntity =  projectCsvParser.parseLine(parts[LogsColumnIndexForProject.CURRENT_ENTITY]),
            createdAt = LocalDateTime.parse(parts[LogsColumnIndexForProject.CREATED_AT])
        )
    }



    fun smartCsvSplit(input: String): List<String> {
        val cleanedLine = input.removeSurrounding("[", "]")
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var bracketDepth = 0

        for (char in cleanedLine) {
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
                        result.add(current.toString().trim())
                        current.clear()
                    } else {
                        current.append(char)
                    }
                }
                else -> current.append(char)
            }
        }

        if (current.isNotEmpty()) {
            result.add(current.toString().trim())
        }

        return result.filter { it.isNotEmpty() }
    }

}

