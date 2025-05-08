package org.example.data.csv.state_csv

import CsvParser
import org.example.models.State
import java.util.*

class StateCsvParser : CsvParser<State> {

    override fun parseFile(csvLines: List<String>): List<State> {
        if (csvLines.isEmpty())
            return emptyList()
        return csvLines.map { parseLine(it) }
    }

    override fun parseLine(line: String): State {
        val cleanedLine = line.replace(" ", "")

        val cleanLine = line.removeSurrounding("[", "]")
        val parts = cleanLine.split(",").map { it.trim() }

        return State(
            id = UUID.fromString(parts[StateColumnIndex.ID]),
            name = parts[StateColumnIndex.NAME]
        )
    }

}