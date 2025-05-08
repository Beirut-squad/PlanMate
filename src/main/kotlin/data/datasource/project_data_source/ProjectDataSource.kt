package org.example.data.datasource.project_data_source

import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.util.*

interface ProjectDataSource {
    fun createProject(project: Project)
    fun editProject(project: Project)
    fun deleteProject(id: UUID)
    fun getAllProjects(): List<Project>
    fun getProject(id: UUID): Project
    fun addStateToProject(projectId: UUID, state: State): Project
    fun editStateToProject(projectId: UUID, state: State): Project
    fun removeStateFromProject(projectId: UUID, state: State): Project
    fun getProjectForMateByUserId(userId: UUID): List<Project>
    fun addMateToProject(projectId: UUID, user: User)
    fun getProjectsForUserById(userId: UUID): List<Project>

}