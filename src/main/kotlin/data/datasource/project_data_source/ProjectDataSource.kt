package org.example.data.datasource.project_data_source

import org.example.models.Project
import org.example.models.State
import java.util.*

interface ProjectDataSource {
    fun createProject()
    fun editProject(project: Project)
    fun deleteProject(project: Project)
    fun getAllProjects(): List<Project>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<String>
    fun editStateToProject(projectId: UUID, state: State): Result<String>
    fun removeStateFromProject(projectId: UUID, state: State): Result<String>
}