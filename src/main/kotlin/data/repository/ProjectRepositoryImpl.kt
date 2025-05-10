package org.example.data.repository

import domain.model.Project
import domain.model.State
import domain.model.User
import org.example.data.datasource.ProjectDataSource
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
        return projectDataSource.addState(projectId, state)
    }

    override suspend fun editStateToProject(projectId: UUID, state: State): Project {
        return projectDataSource.editState(projectId, state)
    }

    override suspend fun removeStateFromProject(projectId: UUID, state: State): Project {
        return projectDataSource.deleteState(projectId, state)
    }

    override suspend fun getProjectForMateByUserId(userId: UUID): List<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun addMateToProject(projectId: UUID, user: User): Project {
        return projectDataSource.addMate(projectId, user)
    }

    override suspend fun getProjectsForUserById(userid: UUID): List<Project> {
        return projectDataSource.getUserProjectsById(userid)
    }

}