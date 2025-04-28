package org.example.data.datasource.project_data_source

import org.example.models.Project
import java.util.*

interface ProjectDataSource {
    fun createProject()
    fun editProject(project: Project)
    fun deleteProject(project: Project)
    fun fetchAllProjects(): List<Project>
    fun fetchProject(id: UUID): Project
    fun editStateToProject(projectId: String, state: String): Result<Project>
    fun addStateToProject(projectId: String, state: String): Result<Project>
    fun removeStateFromProject(projectId: String, state: String): Result<Project>
}