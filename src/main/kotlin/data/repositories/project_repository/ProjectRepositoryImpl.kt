package org.example.data.repositories.project_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project

class ProjectRepositoryImpl(
    projectDataSource: ProjectDataSource,
    logDataSource: LogDataSource
) : ProjectRepository {
    override fun createProject() {
        TODO("Not yet implemented")
    }

    override fun editProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun fetchAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun fetchProject(): Project {
        TODO("Not yet implemented")
    }

    override fun addStateToProject(projectId: String, state: String): Result<Project> {
        TODO("Not yet implemented")
    }

    override fun editStateToProject(projectId: String, state: String): Result<Project> {
        TODO("Not yet implemented")
    }

    override fun removeStateFromProject(projectId: String, state: String): Result<Project> {
        TODO("Not yet implemented")
    }

}