package org.example.data.csv.parser

import org.example.data.model.Project
import org.example.data.csv.column_index.ProjectColumnIndex
import org.example.data.csv.helper.smartCsvSplit
import org.example.data.model.State
import org.example.data.model.User
import java.time.LocalDateTime
import java.util.*

class ProjectParser(private val stateCsvParser: CsvParser<State>,
                    private val userCsvParser: CsvParser<User>
) : CsvParser<Project> {


    override fun parseFile(csvLines: List<String>): List<Project> {
        if (csvLines.isEmpty())
            return emptyList()
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }

    override fun parseLine(line: String): Project? {
        var cleanedLine = line.replace(" ", "")
        if (cleanedLine == "[]" || cleanedLine == "")
            return null

        cleanedLine = line.removeSurrounding("[", "]")
        val parts = smartCsvSplit(cleanedLine)

        if (stateCsvParser.parseLine(parts[ProjectColumnIndex.STATE]) == null){
            throw Exception("no states in the project")
        }
        return Project(
            id = UUID.fromString(parts[ProjectColumnIndex.PROJECT_ID]),
            title = parts[ProjectColumnIndex.NAME],
            description = parts[ProjectColumnIndex.DESCRIPTION],
            creatorUserID = UUID.fromString(parts[ProjectColumnIndex.CREATOR_USER_ID]),
            createdAt = LocalDateTime.parse(parts[ProjectColumnIndex.CREATED_AT]),
            updatedAt = LocalDateTime.parse(parts[ProjectColumnIndex.UPDATED_AT]),
            users = parseMultiUser(parts[ProjectColumnIndex.USER]),
            state = parseMultiStates(parts[ProjectColumnIndex.STATE]),
            )
    }
     private fun parseMultiStates(line: String): List<State> {
        val statesLines = smartCsvSplit(line)
        return stateCsvParser.parseFile(statesLines)
    }
    private fun parseMultiUser(line: String): List<User> {
        val userLines = smartCsvSplit(line)
        return userCsvParser.parseFile(userLines)
    }
}