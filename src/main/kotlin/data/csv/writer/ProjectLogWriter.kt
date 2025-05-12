package org.example.data.csv.writer

import org.example.core.domain.exception.InvalidFileNameException
import domain.model.ProjectLog
import org.example.data.csv.helper.isValidFileName
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class ProjectLogWriter : CsvWriter<ProjectLog> {
    override suspend fun writeToFile(items: List<ProjectLog>, filePath: String) {
        if (items.isNotEmpty()) {
            val file = File("src/main/kotlin/$filePath")
            if (!isValidFileName(file.name))
                throw InvalidFileNameException()
            val writer = BufferedWriter(FileWriter(file))
            writeProjectLog(items, writer)
            writer.close()
        }
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