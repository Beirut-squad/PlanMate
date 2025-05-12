package org.example.data.csv.writer

import org.example.core.domain.exception.InvalidFileNameException
import org.example.data.csv.helper.isValidFileName
import domain.model.State
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class StateWriter : CsvWriter<State> {
    override suspend fun writeToFile(items: List<State>, filePath: String) {
        val file = File(filePath)
        if (!isValidFileName(file.name))
            throw InvalidFileNameException()
        val writer = BufferedWriter(FileWriter(file))
        if (file.length() == 0L)
            writer.write("[id,name]\n")
        if (items.isNotEmpty())
            writeState(items, writer)
        writer.close()
    }

    private fun writeState(items: List<State>, writer: BufferedWriter) {
        items.forEach { state ->
            if (isValidState(state))
                writer.write("[${state.id},${state.name}]\n")
        }
    }

    private fun isValidState(state: State): Boolean {
        return state.name.isNotBlank() && state.id != UUID(0, 0)
    }
}