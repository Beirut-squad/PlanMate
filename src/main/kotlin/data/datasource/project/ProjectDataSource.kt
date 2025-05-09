package data.datasource.project

import org.example.data.model.Project
import org.example.data.model.State
import org.example.data.model.User
import java.util.*

interface ProjectDataSource {
    suspend fun createProject(project: Project)
    suspend fun editProject(project: Project)
    suspend fun deleteProject(id: UUID)
    suspend fun getAllProjects(): List<Project>
    suspend fun getProject(id: UUID): Project
    suspend fun addStateToProject(projectId: UUID, state: State): Project
    suspend fun editStateToProject(projectId: UUID, state: State): Project
    suspend fun removeStateFromProject(projectId: UUID, state: State): Project
    suspend fun getProjectForMateByUserId(userId: UUID): List<Project>
    suspend fun addMateToProject(projectId: UUID, user: User): Project
    suspend fun getProjectsForUserById(userId: UUID): List<Project>

}