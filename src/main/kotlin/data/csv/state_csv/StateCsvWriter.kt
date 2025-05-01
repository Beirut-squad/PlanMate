package org.example.data.csv.state_csv

import org.example.data.csv.CsvWriter
import org.example.models.State

class StateCsvWriter: CsvWriter<State> {
    override fun writeToFile(items: List<State>, filePath: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}