package org.example.logic.repositories.project_repository

import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.util.UUID

interface ProjectRepository {
    fun createProject(project: Project) :Result<Unit>
    fun editProject(project: Project) :Result<Unit>
    fun deleteProject(id: UUID) :Result<Unit>
    fun getAllProjects() : Result<List<Project>>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<Unit>
    fun editStateToProject(projectId: UUID, state: State): Result<Unit>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Unit>
    fun getProjectForMateByUserId(userId : UUID):Result<List<Project>>
    fun addMateToProject(projectId: UUID, user: User)
}