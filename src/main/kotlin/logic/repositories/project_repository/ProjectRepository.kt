package org.example.logic.repositories.project_repository

import org.example.models.Project
import org.example.models.State
import java.util.UUID

interface ProjectRepository {
    suspend fun createProject(project: Project)
    fun editProject(project: Project) :Result<Unit>
    fun deleteProject(id: UUID) :Result<Unit>
    suspend fun getAllProjects() : List<Project>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<Unit>
    fun editStateToProject(projectId: UUID, state: State): Result<Unit>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Unit>
}