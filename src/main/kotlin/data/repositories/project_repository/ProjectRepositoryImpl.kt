package org.example.data.repositories.project_repository

import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
) : ProjectRepository {

    override fun createProject(project: Project): Result<Unit> {
        return projectDataSource.createProject(project)
    }

    override fun editProject(project: Project): Result<Unit> {
        return projectDataSource.editProject(project)
    }

    override fun deleteProject(id: UUID): Result<Unit> {
        return projectDataSource.deleteProject(id)
    }

    override fun getAllProjects(): Result<List<Project>> {
        return projectDataSource.getAllProjects()
    }

    override fun getProject(id: UUID): Result<Project> {
        return projectDataSource.getProject(id)
    }

    override fun addStateToProject(projectId: UUID, state: State): Result<Project> {
        return projectDataSource.addStateToProject(projectId, state)
    }

    override fun editStateToProject(projectId: UUID, state: State): Result<Project> {
        return projectDataSource.editStateToProject(projectId, state)
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Project> {
        return projectDataSource.removeStateFromProject(projectId, state)
    }

    override fun getProjectForMateByUserId(userId: UUID): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun addMateToProject(projectId: UUID, user: User){
        return projectDataSource.addMateToProject(projectId, user)
    }

}