package org.example.fake_datasource

import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.time.LocalDateTime
import java.util.*

class ProjectDataSourceFakeImpl : ProjectDataSource {
    private val projects = mutableListOf<Project>()

    override fun createProject(project: Project): Result<Unit> {
        projects.add(project)
        return Result.success(Unit)
    }

    override fun editProject(project: Project): Result<Unit> {
        return projects.removeIf { it.id == project.id }
            .let {
                if (it) {
                    projects.add(project)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("No project found"))
                }
            }
    }

    override fun deleteProject(id: UUID): Result<Unit> {
        return projects.removeIf { it.id == id }
            .let {
                if (it) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("No project found"))
                }
            }
    }

    override fun getAllProjects(): Result<List<Project>> {
        return Result.success(projects)
    }

    override fun getProject(id: UUID): Result<Project> {
        return projects.find { it.id == id }
            ?.let {
                Result.success(it)
            }
            ?: Result.failure(Exception())
    }

    override fun addStateToProject(projectId: UUID, state: State): Result<Project> {
        return projects.find { it.id == projectId }?.let {
            Result.success(it)
        }.also {
            updateProjectState(projectId, state)
        } ?: Result.failure(Exception())
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

    override fun editStateToProject(projectId: UUID, state: State): Result<Project> {
        return projects.find { it.id == projectId }?.let {
            Result.success(it)
        }.also {
            editProjectState(projectId, state)
        } ?: Result.failure(Exception())
    }

    private fun editProjectState(projectId: UUID, state: State) {
        projects.replaceAll { project ->
            if (project.id == projectId) {
                project.copy(
                    state = project.state.map { if (it.id == state.id) state else it }
                )
            } else {
                project
            }
        }
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Project> {
        return projects.find { it.id == projectId }?.let {
            Result.success(it)
        }.also {
            deleteProjectState(projectId, state)
        } ?: Result.failure(Exception())
    }

    override fun getProjectsForUserById(userid: UUID): Result<List<Project>> {
        return runCatching {
            projects.filter { projects ->
                projects.users.find { it.id == userid } != null
            }
        }
    }

    override fun getProjectForMateByUserId(userId: UUID): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun addMateToProject(projectId: UUID, user: User) {
        projects.replaceAll { project ->
            if (project.id == projectId) {
                project.copy(
                    users = if (project.users.contains(user)) {
                        project.users
                    } else {
                        project.users + user
                    }
                )
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