package data.repository

import domain.model.Project
import domain.model.TaskState
import domain.model.User
import data.datasource.ProjectDataSource
import domain.repository.ProjectRepository
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

    override suspend fun addStateToProject(projectId: UUID, taskState: TaskState): Project {
        return projectDataSource.addState(projectId, taskState)
    }

    override suspend fun editStateToProject(projectId: UUID, taskState: TaskState): Project {
        return projectDataSource.editState(projectId, taskState)
    }

    override suspend fun deleteStateFromProject(projectId: UUID, taskState: TaskState): Project {
        return projectDataSource.deleteState(projectId, taskState)
    }

    override suspend fun addMateToProject(projectId: UUID, user: User): Project {
        return projectDataSource.addMate(projectId, user)
    }

    override suspend fun getUserProjectsById(userid: UUID): List<Project> {
        return projectDataSource.getUserProjectsById(userid)
    }

}