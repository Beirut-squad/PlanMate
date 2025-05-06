package org.example.data.datasource.project_data_source

import org.example.models.Project
import org.example.models.State
import java.util.*

interface ProjectDataSource {
    suspend fun createProject(project: Project)
    fun editProject(project: Project) :Result<Unit>
    suspend fun deleteProject(id: UUID)
    suspend fun getAllProjects() : List<Project>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<Unit>
    fun editStateToProject(projectId: UUID, state: State): Result<Unit>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Unit>
}