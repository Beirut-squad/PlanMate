package org.example.data.csv.writer

import org.example.core.domain.exception.InvalidFileNameException
import org.example.data.csv.helper.isValidFileName
import domain.model.TaskLog
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class TaskLogWriter : CsvWriter<TaskLog> {
    override suspend fun writeToFile(items: List<TaskLog>, filePath: String) {
        val file = File(filePath)
        if (!isValidFileName(file.name))
            throw InvalidFileNameException()
        val writer = BufferedWriter(FileWriter(file))
        if (file.length() == 0L)
            writer.write("[id,userId,entityId,previousEntity,currentEntity,createdAt]\n")
        if (items.isNotEmpty())
            writeTaskLog(items, writer)
        writer.close()
    }

    private fun writeTaskLog(items: List<TaskLog>, writer: BufferedWriter) {
        items.forEach { taskLog ->
            if (isValidTaskLog(taskLog))
                writer.write("${taskLog.id},${taskLog.userId},${taskLog.entityId},${taskLog.currentEntity},${taskLog.currentEntity},${taskLog.createdAt}\n")
        }
    }

    private fun isValidTaskLog(taskLog: TaskLog): Boolean {
        return taskLog.id != UUID(0, 0) && taskLog.userId != UUID(0, 0) && taskLog.entityId != UUID(0, 0)
    }
}