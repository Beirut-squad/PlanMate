package org.example.data.datasource.project_data_source

import org.example.models.Project
import org.example.models.State
import java.util.*

interface ProjectDataSource {
    suspend fun createProject(project: Project)
    suspend fun editProject(project: Project)
    suspend fun deleteProject(id: UUID)
    suspend fun getAllProjects(): List<Project>
    suspend fun getProject(id: UUID): Project
    fun addStateToProject(projectId: UUID, state: State): Result<Unit>
    fun editStateToProject(projectId: UUID, state: State): Result<Unit>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Unit>
}