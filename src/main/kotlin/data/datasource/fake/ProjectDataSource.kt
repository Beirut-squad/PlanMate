package data.datasource.fake

import data.datasource.ProjectDataSource
import ui.common.exception.ProjectNotFoundException
import domain.model.Project
import domain.model.TaskState
import domain.model.User
import java.time.LocalDateTime
import java.util.*

class ProjectDataSource : ProjectDataSource {
    private val projects = mutableListOf<Project>()

    override suspend fun createProject(project: Project) {
        projects.add(project)
    }

    override suspend fun editProject(project: Project) {
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


    override suspend fun getAllProjects(): List<Project> {
        return projects
    }

    override suspend fun getProject(id: UUID): Project {
        return projects
            .find { it.id == id }
            ?: throw ProjectNotFoundException()
    }

    override suspend fun addState(projectId: UUID, taskState: TaskState): Project {
        return updateProjectState(projectId, taskState)
    }


    override suspend fun editState(projectId: UUID, taskState: TaskState): Project {
        return editProjectState(projectId, taskState)
    }

    override suspend fun deleteState(projectId: UUID, taskState: TaskState): Project {
        return deleteProjectState(projectId, taskState)
    }

    override suspend fun getUserProjectsById(userId: UUID): List<Project> {
        return projects.filter { project ->
            project.users.any { user -> user.id == userId }
        }
    }

    override suspend fun addMate(projectId: UUID, user: User): Project {
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
        return updatedProject ?: throw ProjectNotFoundException()
    }


    private fun updateProjectState(
        projectId: UUID,
        taskState: TaskState
    ): Project {
        var updatedProject: Project? = null
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val projectUpdated = project.copy(
                    taskStates = project.taskStates + listOf(taskState),
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

    private fun editProjectState(
        projectId: UUID,
        updatedTaskState: TaskState
    ): Project {
        var updatedProject: Project? = null
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val updatedStates = project.taskStates.map { existingState ->
                    if (existingState.id == updatedTaskState.id) updatedTaskState else existingState
                }
                val projectUpdated = project.copy(
                    taskStates = updatedStates,
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

    private fun deleteProjectState(
        projectId: UUID,
        taskState: TaskState
    ): Project {
        var updatedProject: Project? = null
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val projectUpdated = project.copy(
                    taskStates = project.taskStates.filterNot { it.id == taskState.id },
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