package org.example.logic.repositories.project_repository

import org.example.models.Project

interface ProjectRepository {
    fun createProject()
    fun editProject(project: Project)
    fun deleteProject(project: Project)
    fun fetchAllProjects() : List<Project>
    fun fetchProject() : Project
    fun addStateToProject(projectId: String, state: String): Result<Project>
    fun editStateToProject(projectId: String, state: String): Result<Project>
    fun removeStateFromProject(projectId: String, state: String): Result<Project>
}