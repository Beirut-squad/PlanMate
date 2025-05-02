package org.example.data.csv.state_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.State
import org.example.models.TaskLog
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.UUID

class StateCsvWriter: CsvWriter<State> {
    override fun writeToFile(items: List<State>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File(filePath)
            if (!isValidFileName(file.name))
                throw IllegalArgumentException("Error: Invalid file name")
            val writer = BufferedWriter(FileWriter(file))
            if (file.length() == 0L)
                writer.write("[id,name]\n")
            if (items.isNotEmpty())
                writeState(items, writer)
            writer.close()
        }.fold(
            onSuccess = { return Result.success(Unit) },
            onFailure = { return Result.failure(it) }
        )
    }
    private fun writeState(items: List<State>, writer: BufferedWriter) {
        items.forEach { state ->
            if (isValidState(state))
                writer.write("[${state.id},${state.name}]\n")
        }
    }
    internal fun isValidState(state: State): Boolean {
        return state.name.isNotBlank() && state.id != UUID(0,0)
    }
}