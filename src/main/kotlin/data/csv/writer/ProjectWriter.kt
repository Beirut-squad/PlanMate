package org.example.data.csv.writer

import domain.model.Project
import org.example.data.csv.helper.isValidFileName
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID

class ProjectWriter: CsvWriter<Project> {
    override fun writeToFile(items: List<Project>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File(filePath)
           if(!isValidFileName(file.name))
               throw IllegalArgumentException("Error : Invalid file name")
            val writer = BufferedWriter(FileWriter(file))
            if (items.isNotEmpty())
                writeProject(items,writer)
            writer.close()
        }
    }
    private fun writeProject(items: List<Project>, writer: BufferedWriter) {
        items.forEach { project ->
            if (isValidProject(project))
                writer.write("[${project.id},${project.title},${project.description},${project.createdAt},${project.updatedAt},${project.state},${project.users}]\n")
        }
    }
    internal fun isValidProject(project : Project): Boolean{
        return project.id != UUID(0,0) && project.title.isNotBlank() && project.description.isNotBlank() && project.creatorUserID!= UUID(0,0) && project.state.isNotEmpty()
    }
}

