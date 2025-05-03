package data.csv.project_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.Project
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class ProjectCsvWriter: CsvWriter<Project> {
    override fun writeToFile(items: List<Project>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File(filePath)
           if(!isValidFileName(file.name))
               throw IllegalArgumentException("Error : Invalid file name")
            val writer = BufferedWriter(FileWriter(file))
            if (file.length() == 0L)
                writer.write("[id,name,description,creatorUserID,createdAt,updatedAt,state]\n")
            if (items.isNotEmpty())
                writeProject(items,writer)
            writer.close()
        }
    }
    private fun writeProject(items: List<Project>, writer: BufferedWriter) {
        items.forEach { project ->
            if (isValidProject(project))
                writer.write("[${project.id},${project.name},${project.description},${project.createdAt},${project.updatedAt},${project.state}]\n")
        }
    }
    internal fun isValidProject(project : Project): Boolean{
        return project.id != UUID(0,0) && project.name.isNotBlank() && project.description.isNotBlank() && project.creatorUserID!= UUID(0,0) && project.state.isNotEmpty()
    }
}

