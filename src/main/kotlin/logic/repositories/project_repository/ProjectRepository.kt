package org.example.logic.repositories.project_repository

import org.example.models.Project
import org.example.models.State
import java.util.UUID

interface ProjectRepository {
    fun createProject()
    fun editProject(project: Project)
    fun deleteProject(project: Project)
    fun getAllProjects() : Result<List<Project>>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<Unit>
    fun editStateToProject(projectId: UUID, state: State): Result<Unit>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Unit>
}