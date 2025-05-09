package org.example.data.csv.writer

import org.example.data.model.ProjectLog
import org.example.data.csv.helper.isValidFileName
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class ProjectLogWriter : CsvWriter<ProjectLog> {
    override fun writeToFile(items: List<ProjectLog>, filePath: String): Result<Unit> {
        return runCatching {
            if (items.isNotEmpty()) {
                val file = File("src/main/kotlin/$filePath")
                if (!isValidFileName(file.name))
                    throw IllegalArgumentException("Invalid file name")

//            if (file.length() == 0L)
//                writer.write("[id,userId,entityId,previousEntity,currentEntity,createdAt]\n")

                val writer = BufferedWriter(FileWriter(file))
                writeProjectLog(items, writer)
                writer.close()
            }

        }
    }

    private fun writeProjectLog(items: List<ProjectLog>, writer: BufferedWriter) {
        items.forEach { projectLog ->
            if (isValidProjectLog(projectLog))
                writer.write("${projectLog.id},${projectLog.userId},${projectLog.entityId},${projectLog.currentEntity},${projectLog.currentEntity},${projectLog.createdAt}\n")
        }
    }

    internal fun isValidProjectLog(projectLog: ProjectLog): Boolean {
        return projectLog.id != UUID(0, 0) && projectLog.userId != UUID(0, 0) && projectLog.entityId != UUID(0, 0)
    }
}