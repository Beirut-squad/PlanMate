package data.datasource.csv.writer

import ui.common.exception.InvalidFileNameException
import domain.model.Task
import data.datasource.csv.helper.isValidFileName
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class TaskWriter : CsvWriter<Task> {

    override suspend fun writeToFile(items: List<Task>, filePath: String) {
        val file = File("src/main/kotlin/$filePath")
        if (!isValidFileName(file.name))
            throw InvalidFileNameException()
        val writer = BufferedWriter(FileWriter(file, false))
        writeTask(items, writer)
        writer.close()
    }

    private fun writeTask(items: List<Task>, writer: BufferedWriter) {
        items.forEach { task ->
            if (isValidTask(task))
                writer.write(
                    "${task.id}," +
                            "${task.projectId}," +
                            "${task.title}," +
                            "${task.description}," +
                            "[${task.taskState.id},${task.taskState.name}]," +
                            "${task.creatorUserID}," +
                            "${task.createdAt}," +
                            "${task.updatedAt}\n"
                )
        }
    }

    private fun isValidTask(task: Task): Boolean {
        return task.id != UUID(0, 0) && task.description.isNotBlank()
                && task.creatorUserID != UUID(0, 0)
                && task.title.isNotBlank()
    }
}
