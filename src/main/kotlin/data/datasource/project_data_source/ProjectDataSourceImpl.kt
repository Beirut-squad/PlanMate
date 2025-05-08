package org.example.data.datasource.project_data_source


import data.csv.FileName
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.project_magement_exceptions.*
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
    override suspend fun createProject(project: Project) {
        try {
            buildSuccessCreate(project)
        } catch (e: Exception) {
            throw ProjectNotCreatedException("Failed to create project: ${e.message}")
        }
    }


    override suspend fun editProject(project: Project) {
        try {
            buildSuccessEditor(project)
        } catch (e: Exception) {
            throw ProjectNotEditedException("Failed to edit project: ${e.message}")
        }
    }


    override suspend fun deleteProject(id: UUID) {
        try {
            buildSuccessDelete(id)
        } catch (e: Exception) {
            throw ProjectNotDeletedException("Failed to delete project: ${e.message}")
        }
    }

    override suspend fun getAllProjects(): List<Project> {
        return try {
            csvReader.read(fileName)
        } catch (e: FileNotFoundException) {
            emptyList()
        } catch (e: Exception) {
            throw ProjectNotGetAllProjectsException("Failed to get projects: ${e.message}")
        }
    }


    override suspend fun getProject(id: UUID): Project {
        return csvReader.read(fileName).find { it.id == id } ?: throw NoProjectFoundException()
    }

    override suspend fun addStateToProject(projectId: UUID, state: State): Project {
        return modifyProjectState(projectId) { states ->
            if (states.any { oldState -> haveSameStateName(oldState, state) }) {
                throw DuplicateStateException()
            }
            states + state
        }
    }


    override suspend fun editStateToProject(projectId: UUID, state: State): Project {
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

    override suspend fun removeStateFromProject(projectId: UUID, state: State): Project {
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

    override suspend fun getProjectForMateByUserId(userId: UUID): List<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun addMateToProject(projectId: UUID, user: User): Project {
        return modifyProjectUser(projectId) { users ->
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

    override suspend fun getProjectsForUserById(userId: UUID): List<Project> {
        val project = csvReader.read(fileName).filter { it.creatorUserID == userId }
        return project.ifEmpty { throw NoProjectFoundException() }
    }


    private suspend fun modifyProjectUser(projectId: UUID, userModifier: (List<User>) -> List<User>): Project {
        val projects = getAllProjects()

        val (updatedProjects, updatedProject) = findAndUpdateProject(projects, projectId) { project ->
            project.copy(
                users = userModifier(project.users),
                updatedAt = LocalDateTime.now()
            )
        }

        csvWriter.writeToFile(updatedProjects, fileName)
        return updatedProject ?: throw NoProjectFoundException()
    }


    private suspend fun modifyProjectState(
        projectId: UUID, stateModifier: (List<State>) -> List<State>
    ): Project {
        val projects = getAllProjects()
        val (updatedProject, project) = findAndUpdateProject(projects, projectId) { project ->
            project.copy(state = stateModifier(project.state), updatedAt = LocalDateTime.now())
        }

        csvWriter.writeToFile(updatedProject, fileName)

        return project ?: throw NoProjectFoundException()
    }


    private fun findAndUpdateProject(
        projects: List<Project>, projectId: UUID, projectModifier: (Project) -> Project
    ): Pair<List<Project>, Project?> {
        var updatedProject: Project? = null
        val updated = projects.map { project ->
            if (project.id == projectId) {
                val projectUpdated = projectModifier(project)
                updatedProject = projectUpdated
                projectUpdated
            } else {
                project
            }
        }
        return Pair(updated.toList(), updatedProject)
    }

    private fun haveSameStateName(oldState: State, newState: State): Boolean {
        return oldState.name.equals(newState.name, ignoreCase = true)
    }

    private fun haveSameUserID(oldUser: User, newUser: User): Boolean {
        return oldUser.id == newUser.id
    }

    private suspend fun buildSuccessCreate(project: Project) {
        val existingProjects = getAllProjects()
        if (existingProjects.any { it.name == project.name && it.creatorUserID == project.creatorUserID }) {
            throw ProjectNotCreatedException(
                "Project '${project.name}' already exists for user ${project.creatorUserID}"
            )
        }
        csvWriter.writeToFile(existingProjects + project, fileName)
    }

    private suspend fun buildSuccessEditor(project: Project) {
        val existingProjects = getAllProjects()
        val index = existingProjects.indexOfFirst { it.id == project.id }
        if (index == -1) {
            throw ProjectNotEditedException("Project with ID ${project.id} not found")
        }
        val updatedProjects = existingProjects.toMutableList().apply {
            this[index] = project.copy(updatedAt = LocalDateTime.now())
        }
        csvWriter.writeToFile(updatedProjects, fileName)
    }

    private suspend fun buildSuccessDelete(id: UUID) {
        val existingProjects = getAllProjects()
        if (existingProjects.none { it.id == id }) {
            throw ProjectNotDeletedException("Project with ID $id not found")
        }
        val updatedProjects = existingProjects.filterNot { it.id == id }
        csvWriter.writeToFile(updatedProjects, fileName)
    }
}