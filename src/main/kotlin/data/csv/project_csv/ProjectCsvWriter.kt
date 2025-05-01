package org.example.data.csv.project_csv_parser

import org.example.data.csv.CsvWriter
import org.example.models.Project

class ProjectCsvWriter: CsvWriter<Project> {
    override fun writeToFile(items: List<Project>, filePath: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}