package data.datasource.csv.parser

import data.datasource.csv.column_index.ProjectColumnIndex
import domain.exception.StateNotFoundException
import domain.model.Project
import domain.model.User
import domain.model.State
import java.time.LocalDateTime
import java.util.*

class ProjectParser(
    private val stateCsvParser: CsvParser<State>,
    private val userCsvParser: CsvParser<User>
) : CsvParser<Project> {


    override suspend fun parseFile(csvLines: List<String>): List<Project> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override suspend fun parseLine(line: String): Project? {
        var cleanedLine = line.replace(" ", "")
        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = data.datasource.csv.helper.smartCsvSplit(cleanedLine)

        if (stateCsvParser.parseLine(parts[ProjectColumnIndex.STATE]) == null) {
            throw StateNotFoundException()
        }
        return Project(
            id = UUID.fromString(parts[ProjectColumnIndex.PROJECT_ID]),
            title = parts[ProjectColumnIndex.NAME],
            description = parts[ProjectColumnIndex.DESCRIPTION],
            creatorUserID = UUID.fromString(parts[ProjectColumnIndex.CREATOR_USER_ID]),
            createdAt = LocalDateTime.parse(parts[ProjectColumnIndex.CREATED_AT]),
            updatedAt = LocalDateTime.parse(parts[ProjectColumnIndex.UPDATED_AT]),
            users = parseMultiUser(parts[ProjectColumnIndex.USER]),
            states = parseMultiStates(parts[ProjectColumnIndex.STATE]),
        )
    }

    private suspend fun parseMultiStates(line: String): List<State> {
        val statesLines = data.datasource.csv.helper.smartCsvSplit(line)
        return stateCsvParser.parseFile(statesLines)
    }

    private suspend fun parseMultiUser(line: String): List<User> {
        val userLines = data.datasource.csv.helper.smartCsvSplit(line)
        return userCsvParser.parseFile(userLines)
    }
}