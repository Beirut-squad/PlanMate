package org.example.data.repositories.project_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val logDataSource: LogDataSource
) : ProjectRepository {
    override fun createProject(): Result<String> {
        TODO("Not yet implemented")
    }

    override fun editProject(project: Project): Result<String> {
        TODO("Not yet implemented")
    }

    override fun deleteProject(project: Project): Result<String> {
        TODO("Not yet implemented")
    }

    override fun getAllProjects(): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun getProject(id: UUID): Result<Project> {
        TODO("Not yet implemented")
    }

    override fun addStateToProject(projectId: UUID, state: State): Result<Project> {
        TODO("Not yet implemented")
    }

    override fun editStateToProject(projectId: UUID, state: State): Result<Project> {
        TODO("Not yet implemented")
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Project> {
        TODO("Not yet implemented")
    }


}