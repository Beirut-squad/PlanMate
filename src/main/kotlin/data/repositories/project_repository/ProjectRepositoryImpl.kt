package org.example.data.repositories.project_repository

import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.logic.exceptions.ProjectNotCreatedException
import org.example.logic.exceptions.ProjectNotDeletedException
import org.example.logic.exceptions.ProjectNotEditedException
import org.example.logic.exceptions.ProjectNotGetAllProjectsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val project: Project
) : ProjectRepository {

    override fun createProject(project: Project): Result<String> {
        return projectDataSource.createProject(project).fold(
            onSuccess = {
                Result.success("Project created successfully")
            }, onFailure = {
                Result.failure(ProjectNotCreatedException("Project could not be created"))
            })
    }

    override fun editProject(project: Project): Result<String> {
        return projectDataSource.editProject(project).fold(
            onSuccess = {
                Result.success("Project edited successfully")
            }, onFailure = {
                Result.failure(ProjectNotEditedException("Project could not be edited"))
            })
    }

    override fun deleteProject(project: Project): Result<String> {
        return projectDataSource.deleteProject(project).fold(
            onSuccess = {
                Result.success("Project deleted successfully")
            }, onFailure = {
                Result.failure(ProjectNotDeletedException("Project could not be deleted"))
            })
    }

    override fun getAllProjects(): Result<List<Project>> {
        return projectDataSource.getAllProjects().fold(
            onSuccess = {
                Result.success(listOf(project, project))
            }, onFailure = {
                Result.failure(ProjectNotGetAllProjectsException("you have no projects to show"))
            })    }

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