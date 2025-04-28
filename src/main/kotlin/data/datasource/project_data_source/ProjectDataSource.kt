package org.example.data.datasource.project_data_source

import org.example.models.Project
import org.example.models.State
import java.util.*

interface ProjectDataSource {
    fun createProject() :Result<String>
    fun editProject(project: Project) :Result<String>
    fun deleteProject(project: Project) :Result<String>
    fun getAllProjects() : Result<List<Project>>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<Project>
    fun editStateToProject(projectId: UUID, state: State): Result<Project>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Project>
}