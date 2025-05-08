package org.example.fake_datasource

import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.util.*

class ProjectDataSourceFakeImpl : ProjectDataSource {
    private val projects = mutableListOf<Project>()

    override fun createProject(project: Project) {
        projects.add(project)
    }

    override fun editProject(project: Project) {
        val wasRemoved = projects.removeIf { it.id == project.id }
        if (wasRemoved) {
            projects.add(project)
        } else {
            throw Exception("No project found with ID: ${project.id}")
        }
    }


    override fun deleteProject(id: UUID) {
        val wasRemoved = projects.removeIf { it.id == id }
        if (!wasRemoved) {
            throw Exception("No project found with ID: $id")
        }
    }


    override fun getAllProjects(): List<Project> {
        return projects
    }

    override fun getProject(id: UUID): Project {
        return projects.find { it.id == id } ?: throw Exception()
    }

    override fun addStateToProject(projectId: UUID, state: State): Project {
        val project = projects.find { it.id == projectId } ?: throw Exception()
        updateProjectState(projectId, state)
        return project
    }


    override fun editStateToProject(projectId: UUID, state: State): Project {
        val project = projects.find { it.id == projectId } ?: throw Exception()
        editProjectState(projectId, state)
        return project
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Project {
        val project = projects.find { it.id == projectId } ?: throw Exception()
        deleteProjectState(projectId, state)
        return project
    }

    override fun getProjectsForUserById(userId: UUID): List<Project> {
        return projects.filter { project ->
            project.users.any { user -> user.id == userId }
        }
    }


    override fun getProjectForMateByUserId(userId: UUID): List<Project> {
        TODO("Not yet implemented")
    }

    override fun addMateToProject(projectId: UUID, user: User) {
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val updatedUsers = if (user in project.users) project.users else project.users + user
                project.copy(users = updatedUsers)
            } else {
                project
            }
        }
    }


    private fun updateProjectState(projectId: UUID, state: State) {
        projects.replaceAll { project ->
            if (project.id == projectId) {
                project.copy(
                    state = project.state + listOf(state)
                )
            } else {
                project
            }
        }
    }

    private fun editProjectState(projectId: UUID, updatedState: State) {
        projects.replaceAll { project ->
            if (project.id == projectId) {
                val updatedStates = project.state.map { existingState ->
                    if (existingState.id == updatedState.id) updatedState else existingState
                }
                project.copy(state = updatedStates)
            } else {
                project
            }
        }
    }

    private fun deleteProjectState(projectId: UUID, state: State) {
        projects.replaceAll { project ->
            if (project.id == projectId) {
                project.copy(
                    state = project.state.filterNot { it.id == state.id }
                )
            } else {
                project
            }
        }
    }
}