package org.example.data.datasource.project_data_source

import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.util.*

interface ProjectDataSource {
    fun createProject(project: Project) :Result<Unit>
    fun editProject(project: Project) :Result<Unit>
    fun deleteProject(id: UUID) :Result<Unit>
    fun getAllProjects() : Result<List<Project>>
    fun getProject(id:UUID) : Result<Project>
    fun addStateToProject(projectId: UUID, state: State): Result<Project>
    fun editStateToProject(projectId: UUID, state: State): Result<Project>
    fun removeStateFromProject(projectId: UUID, state: State): Result<Project>
    fun getProjectForMateByUserId(userId : UUID):Result<List<Project>>
    fun addMateToProject(projectId: UUID, user: User)
    fun getProjectsForUserById(userid : UUID):Result<List<Project>>

}