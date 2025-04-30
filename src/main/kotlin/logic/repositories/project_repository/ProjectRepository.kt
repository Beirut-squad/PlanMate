package org.example.logic.repositories.project_repository

import org.example.models.Project
import org.example.models.State
import java.util.UUID

interface ProjectRepository {
    fun createProject(project: Project) :Result<String>
    fun editProject(project: Project) :Result<String>
    fun deleteProject(project: Project) :Result<String>
    fun getAllProjects() : Result<List<Project>>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<Project>
    fun editStateToProject(projectId: UUID, state: State): Result<Project>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Project>
}