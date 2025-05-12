package data.datasource.csv.parser

import data.datasource.csv.column_index.ProjectLogColumnIndex
import domain.exception.CsvValidationException
import domain.model.ProjectLog
import java.time.LocalDateTime
import java.util.*

class ProjectLogParser(private val projectParser: ProjectParser): CsvParser<ProjectLog> {
    override suspend fun parseFile(csvLines: List<String>): List<ProjectLog> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override suspend fun parseLine(line: String): ProjectLog? {
        var cleanedLine = line.replace(" ", "")
        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = data.datasource.csv.helper.smartCsvSplit(cleanedLine)

        if (projectParser.parseLine(parts[ProjectLogColumnIndex.PREVIOUS_ENTITY]) == null || projectParser.parseLine(parts[ProjectLogColumnIndex.CURRENT_ENTITY]) == null)
            throw CsvValidationException()

        return ProjectLog(
            id = UUID.fromString(parts[ProjectLogColumnIndex.ID]),
            userId = UUID.fromString(parts[ProjectLogColumnIndex.USER_ID]),
            entityId = UUID.fromString(parts[ProjectLogColumnIndex.ENTITY_ID]),
            previousEntity = projectParser.parseLine(parts[ProjectLogColumnIndex.PREVIOUS_ENTITY]),
            currentEntity =  projectParser.parseLine(parts[ProjectLogColumnIndex.CURRENT_ENTITY]),
            createdAt = LocalDateTime.parse(parts[ProjectLogColumnIndex.CREATED_AT])
        )
    }
}

