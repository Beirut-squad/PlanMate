package org.example.fake_datasource

import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.logic.exceptions.project_magement_exceptions.NoProjectFoundException
import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.time.LocalDateTime
import java.util.*

class ProjectDataSourceFakeImpl : ProjectDataSource {
    private val projects = mutableListOf<Project>()

    override suspend fun createProject(project: Project) {
        projects.add(project)
    }

    override suspend fun editProject(project: Project) {
        val wasRemoved = projects.removeIf { it.id == project.id }
        if (wasRemoved) {
            projects.add(project)
        } else {
            throw Exception("No project found with ID: ${project.id}")
        }
    }


    override suspend fun deleteProject(id: UUID) {
        val wasRemoved = projects.removeIf { it.id == id }
        if (!wasRemoved) {
            throw Exception("No project found with ID: $id")
        }
    }


    override suspend fun getAllProjects(): List<Project> {
        return projects
    }

    override suspend fun getProject(id: UUID): Project {
        return projects.find { it.id == id } ?: throw Exception()
    }

    override suspend fun addStateToProject(projectId: UUID, state: State): Project {
        return updateProjectState(projectId, state)
    }


    override suspend fun editStateToProject(projectId: UUID, state: State): Project {
        return editProjectState(projectId, state)
    }

    override suspend fun removeStateFromProject(projectId: UUID, state: State): Project {
        return deleteProjectState(projectId, state)
    }

    override suspend fun getProjectsForUserById(userId: UUID): List<Project> {
        return projects.filter { project ->
            project.users.any { user -> user.id == userId }
        }
    }


    override suspend fun getProjectForMateByUserId(userId: UUID): List<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun addMateToProject(projectId: UUID, user: User): Project {
        var updatedProject: Project? = null
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val updatedUsers = if (user in project.users) project.users else project.users + user
                val projectUpdated = project.copy(
                    users = updatedUsers,
                    updatedAt = LocalDateTime.now()
                )
                updatedProject = projectUpdated
                projectUpdated
            } else {
                project
            }
        }
        return updatedProject ?: throw NoProjectFoundException()
    }


    private fun updateProjectState(projectId: UUID, state: State): Project {
        var updatedProject: Project? = null
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val projectUpdated = project.copy(
                    state = project.state + listOf(state),
                    updatedAt = LocalDateTime.now()
                )
                updatedProject = projectUpdated
                projectUpdated
            } else {
                project
            }
        }
        return updatedProject ?: throw NoProjectFoundException()
    }

    private fun editProjectState(projectId: UUID, updatedState: State): Project {
        var updatedProject: Project? = null
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val updatedStates = project.state.map { existingState ->
                    if (existingState.id == updatedState.id) updatedState else existingState
                }
                val projectUpdated = project.copy(
                    state = updatedStates,
                    updatedAt = LocalDateTime.now()
                )
                updatedProject = projectUpdated
                projectUpdated
            } else {
                project
            }
        }
        return updatedProject ?: throw NoProjectFoundException()
    }

    private fun deleteProjectState(projectId: UUID, state: State): Project {
        var updatedProject: Project? = null
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val projectUpdated = project.copy(
                    state = project.state.filterNot { it.id == state.id },
                    updatedAt = LocalDateTime.now()
                )
                updatedProject = projectUpdated
                projectUpdated
            } else {
                project
            }
        }
        return updatedProject ?: throw NoProjectFoundException()
    }
}