package org.example.data.csv.writer

import data.exception.InvalidFileNameException
import domain.exception.handler.ExceptionHandler
import domain.model.ProjectLog
import org.example.data.csv.helper.isValidFileName
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class ProjectLogWriter(
    private val exceptionHandler: ExceptionHandler,
) : CsvWriter<ProjectLog> {
    override suspend fun writeToFile(items: List<ProjectLog>, filePath: String) {
        exceptionHandler.tryCatchingAsync(
            action = {
                if (items.isNotEmpty()) {
                    val file = File("src/main/kotlin/$filePath")
                    if (!isValidFileName(file.name))
                        throw InvalidFileNameException()
                    val writer = BufferedWriter(FileWriter(file))
                    writeProjectLog(items, writer)
                    writer.close()
                }
            }
        )
    }

    private fun writeProjectLog(items: List<ProjectLog>, writer: BufferedWriter) {
        items.forEach { projectLog ->
            if (isValidProjectLog(projectLog))
                writer.write("${projectLog.id},${projectLog.userId},${projectLog.entityId},${projectLog.currentEntity},${projectLog.currentEntity},${projectLog.createdAt}\n")
        }
    }

    private fun isValidProjectLog(projectLog: ProjectLog): Boolean {
        return projectLog.id != UUID(0, 0) && projectLog.userId != UUID(0, 0) && projectLog.entityId != UUID(0, 0)
    }
}