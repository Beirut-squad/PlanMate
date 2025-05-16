package domain.repository

import domain.model.Project
import domain.model.TaskState
import domain.model.User
import java.util.UUID

interface ProjectRepository {
    suspend fun createProject(project: Project)
    suspend fun editProject(project: Project)
    suspend fun deleteProject(id: UUID)
    suspend fun getAllProjects() : List<Project>
    suspend fun getProject(id:UUID) : Project
    suspend fun addStateToProject(projectId: UUID, taskState: TaskState): Project
    suspend fun editStateToProject(projectId: UUID, taskState: TaskState): Project
    suspend fun deleteStateFromProject(projectId: UUID, taskState: TaskState): Project
    suspend fun addMateToProject(projectId: UUID, user: User): Project
    suspend fun getUserProjectsById(userid : UUID): List<Project>
}