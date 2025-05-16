package data.datasource.csv.writer

import ui.common.exception.InvalidFileNameException
import data.datasource.csv.helper.isValidFileName
import domain.model.TaskState
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class StateWriter : CsvWriter<TaskState> {
    override suspend fun writeToFile(items: List<TaskState>, filePath: String) {
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

    private fun writeState(items: List<TaskState>, writer: BufferedWriter) {
        items.forEach { state ->
            if (isValidState(state))
                writer.write("[${state.id},${state.name}]\n")
        }
    }

    private fun isValidState(taskState: TaskState): Boolean {
        return taskState.name.isNotBlank() && taskState.id != UUID(0, 0)
    }
}