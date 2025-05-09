package org.example.data.csv.writer

import domain.model.Task
import org.example.data.csv.helper.isValidFileName
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class TaskWriter : CsvWriter<Task> {

    override fun writeToFile(items: List<Task>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File("src/main/kotlin/$filePath")
            if (!isValidFileName(file.name))
                throw IllegalArgumentException("Invalid file name")


            val writer = BufferedWriter(FileWriter(file, false))
            writeTask(items, writer)
            writer.close()
        }
    }

    private fun writeTask(items: List<Task>, writer: BufferedWriter) {
        items.forEach { task ->
            if (isValidTask(task))
                writer.write(
                    "${task.id}," +
                            "${task.projectId}," +
                            "${task.title}," +
                            "${task.description}," +
                            "[${task.state.id},${task.state.name}]," +
                            "${task.creatorUserID}," +
                            "${task.createdAt}," +
                            "${task.updatedAt}\n"
                )
        }
    }

    internal fun isValidTask(task: Task): Boolean {
        return task.id != UUID(0, 0) && task.description.isNotBlank()
                && task.creatorUserID != UUID(0, 0)
                && task.title.isNotBlank()
    }
}
