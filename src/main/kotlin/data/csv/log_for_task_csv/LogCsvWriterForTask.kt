package org.example.data.csv.log_for_task_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.TaskLog
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class LogCsvWriterForTask : CsvWriter<TaskLog> {
    override fun writeToFile(items: List<TaskLog>, filePath: String) {
         try {
            val file = File(filePath)
            if (!isValidFileName(file.name))
                throw IllegalArgumentException("Invalid file name")
            val writer = BufferedWriter(FileWriter(file))
            if (file.length() == 0L)
                writer.write("[id,userId,entityId,previousEntity,currentEntity,createdAt]\n")
            if (items.isNotEmpty())
                writeTaskLog(items, writer)
            writer.close()
        } catch (error: Exception) {
            println("Failed to write file: ${error.message}")
            throw error
        }
    }

    private fun writeTaskLog(items: List<TaskLog>, writer: BufferedWriter) {
        items.forEach { taskLog ->
            if (isValidTaskLog(taskLog))
                writer.write("${taskLog.id},${taskLog.userId},${taskLog.entityId},${taskLog.currentEntity},${taskLog.currentEntity},${taskLog.createdAt}\n")
        }
    }

    internal fun isValidTaskLog(taskLog: TaskLog): Boolean {
        return taskLog.id != UUID(0, 0) && taskLog.userId != UUID(0, 0) && taskLog.entityId != UUID(0, 0)
    }
}