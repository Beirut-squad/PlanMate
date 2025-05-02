package org.example.data.csv.log_for_task_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.TaskLog
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class LogCsvWriterForTask : CsvWriter<TaskLog> {
    override fun writeToFile(items: List<TaskLog>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File(filePath)
            if(!isValidFileName(file.name)){
                throw IllegalArgumentException("Invalid file name")
            }
    val writer = BufferedWriter(FileWriter(file))
            if (file.length() == 0L){
                writer.write("[id,userId,entityId,previousEntity,currentEntity,createdAt]\n")
            }
            if (items.isNotEmpty()){
                for (taskLog in items){
                    if (isValidTaskLog(taskLog)){
                        writer.write("[${taskLog.id},${taskLog.userId},${taskLog.entityId},${taskLog.previousEntity},${taskLog.currentEntity},${taskLog.createdAt}]\n")
                    }
                }
            }
            writer.close()
        }.fold(
            onSuccess = {
                return Result.success(Unit)
            },
            onFailure = {
                return Result.failure(it)
            }
        )
    }

    internal fun isValidTaskLog(taskLog: TaskLog): Boolean {
       return taskLog.id != UUID(0,0) && taskLog.userId != UUID(0,0) && taskLog.entityId != UUID(0,0)

    }
}