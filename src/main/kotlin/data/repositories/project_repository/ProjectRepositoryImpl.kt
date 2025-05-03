package org.example.data.repositories.project_repository

import CsvParser
import org.example.constants.StringConstants
import org.example.data.csv.CsvReader
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.repositories.log_repository.LogRepositoryImpl
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.exceptions.NoStateException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val project: Project
) : ProjectRepository {

    override fun createProject(project: Project): Result<Unit> {
        return projectDataSource.createProject(project)
    }

    override fun editProject(project: Project): Result<Unit> {
        return projectDataSource.editProject(project)
    }

    override fun deleteProject(id: UUID): Result<Unit> {
        return projectDataSource.deleteProject(project.id)
    }

    override fun getAllProjects(): Result<List<Project>> {
        return projectDataSource.getAllProjects()
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
    override fun getAllProjects(): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun getProject(id: UUID): Result<Project> {
        return projectDataSource.getProject(id)
    }

    override fun addStateToProject(projectId: UUID, state: State): Result<Unit> {
        return projectDataSource.addStateToProject(projectId, state)
    }

    override fun editStateToProject(projectId: UUID, state: State): Result<Unit> {
        return projectDataSource.editStateToProject(projectId, state)
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Unit> {
        return projectDataSource.removeStateFromProject(projectId, state)
    }

}