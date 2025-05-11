package org.example.data.csv.parser

import domain.model.State
import org.example.data.csv.column_index.StateColumnIndex
import java.util.*

class StateParser : CsvParser<State> {

    override suspend fun parseFile(csvLines: List<String>): List<State> {
        if (csvLines.isEmpty())
            return emptyList()
        return csvLines.map { parseLine(it) }
    }

    override suspend fun parseLine(line: String): State {
        val cleanedLine = line.replace(" ", "")

        val cleanLine = line.removeSurrounding("[", "]")
        val parts = cleanLine.split(",").map { it.trim() }

        return State(
            id = UUID.fromString(parts[StateColumnIndex.ID]),
            name = parts[StateColumnIndex.NAME]
        )
    }

}