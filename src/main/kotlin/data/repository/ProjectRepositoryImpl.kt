package org.example.data.repository

import org.example.data.model.Project
import org.example.data.model.State
import org.example.data.model.User
import data.datasource.project.ProjectDataSource
import org.example.domain.repository.ProjectRepository
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    override suspend fun createProject(project: Project) {
        return this.projectDataSource.createProject(project)
    }

    override suspend fun editProject(project: Project) {
        this.projectDataSource.editProject(project)
    }

    override suspend fun deleteProject(id: UUID) {
        projectDataSource.deleteProject(id)
    }

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
    }

    override suspend fun getProject(id: UUID): Project {
        return projectDataSource.getProject(id)
    }

    override suspend fun addStateToProject(projectId: UUID, state: State): Project {
        return projectDataSource.addStateToProject(projectId, state)
    }

    override suspend fun editStateToProject(projectId: UUID, state: State): Project {
        return projectDataSource.editStateToProject(projectId, state)
    }

    override suspend fun removeStateFromProject(projectId: UUID, state: State): Project {
        return projectDataSource.removeStateFromProject(projectId, state)
    }

    override suspend fun getProjectForMateByUserId(userId: UUID): List<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun addMateToProject(projectId: UUID, user: User): Project {
        return projectDataSource.addMateToProject(projectId, user)
    }

    override suspend fun getProjectsForUserById(userid: UUID): List<Project> {
        return projectDataSource.getProjectsForUserById(userid)
    }

}