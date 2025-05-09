package data.csv.log_for_project_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.ProjectLog
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class LogCsvWriterForProject : CsvWriter<ProjectLog> {
    override fun writeToFile(items: List<ProjectLog>, filePath: String) {
        try {
            if (items.isEmpty()) return
            val file = File("src/main/kotlin/$filePath")
            if (!isValidFileName(file.name)) throw IllegalArgumentException("Invalid file name")

            val writer = BufferedWriter(FileWriter(file))
            writeProjectLog(items, writer)
            writer.close()
        } catch (e: Exception) {
            println("Failed to write file: ${e.message}") // Or log properly
            throw e // Re-throw if needed
        }
    }

    private fun writeProjectLog(items: List<ProjectLog>, writer: BufferedWriter) {
        items.forEach { projectLog ->
            if (isValidProjectLog(projectLog)) writer.write("${projectLog.id},${projectLog.userId},${projectLog.entityId},${projectLog.currentEntity},${projectLog.currentEntity},${projectLog.createdAt}\n")
        }
    }

    internal fun isValidProjectLog(projectLog: ProjectLog): Boolean {
        return projectLog.id != UUID(0, 0) && projectLog.userId != UUID(0, 0) && projectLog.entityId != UUID(0, 0)
    }
}