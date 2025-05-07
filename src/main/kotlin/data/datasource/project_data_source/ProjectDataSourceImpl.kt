package org.example.data.datasource.project_data_source


import data.csv.FileName
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotCreatedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotDeletedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotEditedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotGetAllProjectsException
import org.example.logic.exceptions.project_magement_exceptions.DuplicateStateException
import org.example.logic.exceptions.project_magement_exceptions.NoProjectFoundException
import org.example.logic.exceptions.project_magement_exceptions.NoStateException
import org.example.models.Project
import org.example.models.State
import org.example.models.User
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.util.*

class ProjectDataSourceImpl(
    private val csvReader: CsvReader<Project>,
    private val csvWriter: CsvWriter<Project>,
    private val fileName: String = FileName.PROJECTS

) : ProjectDataSource {
    override fun createProject(project: Project): Result<Unit> {
        return try {
            buildSuccessCreate(project)
        } catch (e: Exception) {
            Result.failure(ProjectNotCreatedException("Failed to create project: ${e.message}"))
        }
    }

    override fun editProject(project: Project): Result<Unit> {
        return try {
            buildSuccessEditor(project)
        } catch (e: Exception) {
            Result.failure(ProjectNotEditedException("Failed to edit project: ${e.message}"))
        }
    }


    override fun deleteProject(id: UUID): Result<Unit> {
        return try {
            buildSuccessDelete(id)
        } catch (e: Exception) {
            Result.failure(
                ProjectNotDeletedException("Failed to delete project: ${e.message}")
            )
        }
    }

    override fun getAllProjects(): Result<List<Project>> {
        return try {
            val projects = csvReader.read(fileName)
            Result.success(projects)
        } catch (e: FileNotFoundException) {
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(
                ProjectNotGetAllProjectsException("Failed to get projects: ${e.message}")
            )
        }
    }


    override fun getProject(id: UUID): Result<Project> {
        return runCatching {
            csvReader.read(fileName).find { it.id == id } ?: throw NoProjectFoundException()
        }
    }

    override fun addStateToProject(projectId: UUID, state: State): Result<Project> {
        return modifyProjectState(projectId) { states ->
            if (states.any { oldState -> haveSameStateName(oldState, state) }) throw DuplicateStateException()
            states + state
        }
    }

    override fun editStateToProject(projectId: UUID, state: State): Result<Project> {
        return modifyProjectState(projectId) { states ->
            val updatedStates = mutableListOf<State>()
            var notFoundState = true
            states.forEach { oldState ->
                if (haveSameStateName(oldState, state)) throw DuplicateStateException()
                updatedStates += if (oldState.id == state.id) {
                    notFoundState = false
                    state
                } else oldState
            }
            if (notFoundState) throw NoStateException()
            updatedStates
        }
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Project> {
        return modifyProjectState(projectId) { states ->
            val updatedStates = mutableListOf<State>()
            var notFoundState = true
            states.forEach { oldState ->
                if (oldState.id == state.id) {
                    notFoundState = false
                } else updatedStates += oldState
            }
            if (notFoundState) throw NoStateException()
            updatedStates
        }
    }

    override fun getProjectForMateByUserId(userId: UUID): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun addMateToProject(projectId: UUID, user: User) {
         modifyProjectUser(projectId) { users ->
            val updatedUser = mutableListOf<User>()
            var notFoundUser = true
            users.forEach { oldUsers ->
                if (haveSameUserID(oldUsers, user)) throw DuplicateStateException() //Make Exception For User Duplicate
                updatedUser += if (oldUsers.id == user.id) {
                    notFoundUser = false
                    user
                } else
                    oldUsers
            }
            if (notFoundUser) throw NoStateException() //Make Exception No User
            updatedUser
        }
    }


    private fun modifyProjectUser(projectId: UUID, userModifier: (List<User>) -> List<User>): Result<Unit> {
        return getAllProjects().mapCatching { projects ->
            val updatedProjects = findAndUpdateProject(projects, projectId) { project ->
                project.copy(
                    users = userModifier(project.users),
                    updatedAt = LocalDateTime.now()
                )
            }
            csvWriter.writeToFile(updatedProjects.first, fileName)
        }
    }

    private fun modifyProjectState(projectId: UUID, stateModifier: (List<State>) -> List<State>): Result<Project> {
        return getAllProjects().mapCatching { projects ->
            val result = findAndUpdateProject(projects, projectId) { project ->
                val newProject = project.copy(
                    state = stateModifier(project.state), updatedAt = LocalDateTime.now()
                )
                newProject
            }
            csvWriter.writeToFile(result.first, fileName)
            result.second ?: throw NoProjectFoundException()
        }
    }

    private fun findAndUpdateProject(
        projects: List<Project>, projectId: UUID, projectModifier: (Project) -> Project
    ): Pair<List<Project>, Project?> {
        val updated = mutableListOf<Project>()
        var updatedProject: Project? = null
        for (project in projects) {
            if (project.id == projectId) {
                updatedProject = projectModifier(project)
                updated += updatedProject
            } else {
                updated += project
            }
        }
        return Pair(updated.toList(), updatedProject)
    }

    private fun haveSameStateName(oldState: State, newState: State): Boolean {
        return oldState.name.equals(newState.name, ignoreCase = true)
    }

    private fun haveSameUserID(oldUser: User, newUser: User): Boolean {
        return oldUser.id.equals(newUser.id)
    }

    private fun buildSuccessCreate(project: Project): Result<Unit> {
        val existingProjects = getAllProjects().getOrElse { return Result.failure(it) }
        if (existingProjects.any { it.name == project.name && it.creatorUserID == project.creatorUserID }) {
            return Result.failure(
                ProjectNotCreatedException(
                    "Project '${project.name}' already exists for user ${project.creatorUserID}"
                )
            )
        }
        csvWriter.writeToFile(existingProjects + project, fileName)
        return Result.success(Unit)
    }

    private fun buildSuccessEditor(project: Project): Result<Unit> {
        val existingProjects = getAllProjects().getOrElse { return Result.failure(it) }
        val index = existingProjects.indexOfFirst { it.id == project.id }
        if (index == -1) {
            return Result.failure(
                ProjectNotEditedException("Project with ID ${project.id} not found")
            )
        }
        val updatedProjects = existingProjects.toMutableList().apply {
            this[index] = project.copy(updatedAt = LocalDateTime.now())
        }
        csvWriter.writeToFile(updatedProjects, fileName)
        return Result.success(Unit)
    }

    private fun buildSuccessDelete(id: UUID): Result<Unit> {
        val existingProjects = getAllProjects().getOrElse { return Result.failure(it) }
        if (existingProjects.none { it.id == id }) {
            return Result.failure(
                ProjectNotDeletedException("Project with ID $id not found")
            )
        }
        val updatedProjects = existingProjects.filterNot { it.id == id }
        csvWriter.writeToFile(updatedProjects, fileName)
        return Result.success(Unit)
    }
}