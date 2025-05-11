package data.fake_datasource

import data.exception.ProjectNotFoundException
import domain.model.Project
import domain.model.State
import domain.model.User
import org.example.data.datasource.ProjectDataSource
import java.time.LocalDateTime
import java.util.*

class ProjectFakeDataSource : ProjectDataSource {
    private val projects = mutableListOf<Project>()

    override suspend fun createProject(project: domain.model.Project) {
        projects.add(project)
    }

    override suspend fun editProject(project: domain.model.Project) {
        val wasRemoved = projects.removeIf { it.id == project.id }
        if (wasRemoved) {
            projects.add(project)
        } else {
            throw ProjectNotFoundException()
        }
    }


    override suspend fun deleteProject(id: UUID) {
        val wasRemoved = projects.removeIf { it.id == id }
        if (!wasRemoved) {
            throw ProjectNotFoundException()
        }
    }


    override suspend fun getAllProjects(): List<domain.model.Project> {
        return projects
    }

    override suspend fun getProject(id: UUID): domain.model.Project {
        return projects.find { it.id == id } ?: throw ProjectNotFoundException()
    }

    override suspend fun addState(projectId: UUID, state: State): domain.model.Project {
        return updateProjectState(projectId, state)
    }


    override suspend fun editState(projectId: UUID, state: State): domain.model.Project {
        return editProjectState(projectId, state)
    }

    override suspend fun deleteState(projectId: UUID, state: State): domain.model.Project {
        return deleteProjectState(projectId, state)
    }

    override suspend fun getUserProjectsById(userId: UUID): List<domain.model.Project> {
        return projects.filter { project ->
            project.users.any { user -> user.id == userId }
        }
    }


    override suspend fun getMateProjectsByUserId(userId: UUID): List<domain.model.Project> {
        TODO("Not yet implemented")
    }

    override suspend fun addMate(projectId: UUID, user: User): domain.model.Project {
        var updatedProject: domain.model.Project? = null
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
        return updatedProject ?: throw ProjectNotFoundException()
    }


    private fun updateProjectState(projectId: UUID, state: State): domain.model.Project {
        var updatedProject: domain.model.Project? = null
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
        return updatedProject ?: throw ProjectNotFoundException()
    }

    private fun editProjectState(projectId: UUID, updatedState: State): domain.model.Project {
        var updatedProject: domain.model.Project? = null
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
        return updatedProject ?: throw ProjectNotFoundException()
    }

    private fun deleteProjectState(projectId: UUID, state: State): domain.model.Project {
        var updatedProject: domain.model.Project? = null
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
        return updatedProject ?: throw ProjectNotFoundException()
    }
}