package org.example.data.csv.log_csv_parser

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.ProjectLog
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class LogCsvWriterForProject: CsvWriter<ProjectLog> {
    override fun writeToFile(items: List<ProjectLog>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File(filePath)
            if (!isValidFileName(file.name)) {
                throw IllegalArgumentException("Invalid file name")
            }
            val writer = BufferedWriter(FileWriter(file))
            if (file.length() == 0L){
                writer.write("[id,userId,entityId,previousEntity,currentEntity,createdAt]\n")
            }
            if (items.isNotEmpty()){
                for (projectLog in items){
                    if (isValidProjectLog(projectLog)){
                        writer.write("${projectLog.id},${projectLog.userId},${projectLog.entityId},${projectLog.currentEntity},${projectLog.currentEntity},${projectLog.createdAt}\n")
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

    internal fun isValidProjectLog(projectLog: ProjectLog): Boolean {
        return projectLog.id != UUID(0,0) && projectLog.userId != UUID(0,0) && projectLog.entityId != UUID(0,0)
    }
}