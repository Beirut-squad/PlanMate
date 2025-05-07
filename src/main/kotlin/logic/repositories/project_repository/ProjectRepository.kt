package org.example.logic.repositories.project_repository

import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.util.UUID

interface ProjectRepository {
    fun createProject(project: Project)
    fun editProject(project: Project)
    fun deleteProject(id: UUID)
    fun getAllProjects() : List<Project>
    fun getProject(id:UUID) : Project
    fun addStateToProject(projectId: UUID, state: State): Project
    fun editStateToProject(projectId: UUID, state: State): Project
    fun removeStateFromProject(projectId: UUID, state: State): Project
    fun getProjectForMateByUserId(userId : UUID): List<Project>
    fun addMateToProject(projectId: UUID, user: User)
    fun getProjectsForUserById(userid : UUID): List<Project>
}