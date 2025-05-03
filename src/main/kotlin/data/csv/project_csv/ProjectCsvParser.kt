package data.csv.project_csv

import CsvParser
import org.example.data.csv.smartCsvSplit
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.*

class ProjectCsvParser(private val stateCsvParser: CsvParser<State>) : CsvParser<Project> {


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

        if (parseMultiStates(parts[ProjectColumnIndex.STATE]) == null){
            throw Exception("no states in the project")
        }
        return Project(
            id = UUID.fromString(parts[ProjectColumnIndex.PROJECT_ID]),
            name = parts[ProjectColumnIndex.NAME],
            description = parts[ProjectColumnIndex.DESCRIPTION],
            creatorUserID = UUID.fromString(parts[ProjectColumnIndex.CREATOR_USER_ID]),
            createdAt = LocalDateTime.parse(parts[ProjectColumnIndex.CREATED_AT]),
            updatedAt = LocalDateTime.parse(parts[ProjectColumnIndex.UPDATED_AT]),
            state = parseMultiStates(parts[ProjectColumnIndex.STATE])!!,
            )
    }
     fun parseMultiStates(line: String): List<State>? {
        val statesLines = smartCsvSplit(line)

//         val parsedStates =  stateCsvParser.parseFile(statesLines)
//         if (parsedStates.isEmpty())
//             return null
        return stateCsvParser.parseFile(statesLines)
    }
}