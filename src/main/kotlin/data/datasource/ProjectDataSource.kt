package data.datasource

import domain.model.Project
import domain.model.TaskState
import domain.model.User
import java.util.*

interface ProjectDataSource {
    suspend fun createProject(project: Project)
    suspend fun editProject(project: Project)
    suspend fun deleteProject(id: UUID)
    suspend fun getAllProjects(): List<Project>
    suspend fun getProject(id: UUID): Project
    suspend fun addState(projectId: UUID, taskState: TaskState): Project
    suspend fun editState(projectId: UUID, taskState: TaskState): Project
    suspend fun deleteState(projectId: UUID, taskState: TaskState): Project
    suspend fun addMate(projectId: UUID, user: User): Project
    suspend fun getUserProjectsById(userId: UUID): List<Project>

}