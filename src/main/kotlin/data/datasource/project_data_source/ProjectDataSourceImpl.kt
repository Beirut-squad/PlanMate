package org.example.data.datasource.project_data_source

import FileName
import org.example.data.csv.CsvReader
import org.example.logic.exceptions.NoProjectFoundException
import org.example.models.Project
import org.example.models.State
import java.util.*

class ProjectDataSourceImpl(
    private val reader: CsvReader<Project>,
) : ProjectDataSource {
    private val filename = FileName.PROJECTS_FILE

    override fun createProject() {
        TODO("Not yet implemented")
    }

    override fun editProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun getProject(id: UUID): Result<Project> {
        return runCatching { reader.read(filename) }
            .fold(
                onSuccess = { projects ->
                    projects.find { it.id == id }
                        ?.let { Result.success(it) }
                        ?: Result.failure(NoProjectFoundException())
                },
                onFailure = { Result.failure(it) }
            )
    }

    override fun addStateToProject(projectId: UUID, state: State): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun editStateToProject(projectId: UUID, state: State): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Unit> {
        TODO("Not yet implemented")
    }
}