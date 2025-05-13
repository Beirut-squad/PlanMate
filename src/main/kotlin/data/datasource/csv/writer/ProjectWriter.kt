package data.datasource.csv.writer

import data.datasource.csv.helper.isValidFileName
import ui.common.exception.InvalidFileNameException
import domain.model.Project
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class ProjectWriter : CsvWriter<Project> {
    override suspend fun writeToFile(items: List<Project>, filePath: String) {
        val file = File(filePath)
        if (!isValidFileName(file.name))
            throw InvalidFileNameException()
        val writer = BufferedWriter(FileWriter(file))
        if (items.isNotEmpty())
            writeProject(items, writer)
        writer.close()
    }

    private fun writeProject(items: List<Project>, writer: BufferedWriter) {
        items.forEach { project ->
            if (isValidProject(project))
                writer.write("[${project.id},${project.title},${project.description},${project.createdAt},${project.updatedAt},${project.states},${project.users}]\n")
        }
    }

    private fun isValidProject(project: Project): Boolean {
        return project.id != UUID(
            0,
            0
        ) && project.title.isNotBlank() && project.description.isNotBlank() && project.creatorUserID != UUID(
            0,
            0
        ) && project.states.isNotEmpty()
    }
}

