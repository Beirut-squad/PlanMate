package org.example.data.csv.task_csv_parser

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.Project
import org.example.models.Task
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class TaskCsvWriter : CsvWriter<Task> {
    override fun writeToFile(items: List<Task>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File(filePath)
            if (!isValidFileName(file.name))
                throw IllegalArgumentException("Invalid file name")
            val writer = BufferedWriter(FileWriter(file))
            if (file.length() == 0L)
                writer.write("[id,projectId,title,description,state,creatorUserID,createdAt,updatedAt]\n")
            writeTask(items, writer)
            writer.close()
        }
    }

    private fun writeTask(items: List<Task>, writer: BufferedWriter) {
        items.forEach { task ->
            if (isValidTask(task))
                writer.write("[${task.id}, ${task.projectId}, ${task.title}, ${task.description}, ${task.state},${task.creatorUserID},${task.createdAt},${task.updatedAt}]\n")
        }
    }

    internal fun isValidTask(task: Task): Boolean {
        return task.id != UUID(0, 0) && task.description.isNotBlank()
                && task.creatorUserID != UUID(0, 0)
                && task.title.isNotBlank()
    }
}