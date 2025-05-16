package data.datasource.csv.parser

import data.datasource.csv.column_index.StateColumnIndex
import domain.model.TaskState
import java.util.*

class StateParser : CsvParser<TaskState> {

    override suspend fun parseFile(
        csvLines: List<String>
    ): List<TaskState> {
        if (csvLines.isEmpty())
            return emptyList()
        return csvLines.map { parseLine(it) }
    }

    override suspend fun parseLine(
        line: String
    ): TaskState {
        val cleanLine = line.removeSurrounding("[", "]")
        val parts = cleanLine.split(",").map { it.trim() }

        return TaskState(
            id = UUID.fromString(parts[StateColumnIndex.ID]),
            name = parts[StateColumnIndex.NAME]
        )
    }

}