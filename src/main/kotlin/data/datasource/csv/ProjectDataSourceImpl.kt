package data.datasource.csv

import data.datasource.csv.helper.FileName
import data.datasource.csv.reader.CsvReader
import data.datasource.csv.writer.CsvWriter
import data.datasource.ProjectDataSource
import domain.model.Project
import domain.model.TaskState
import domain.model.User
import ui.common.exception.*
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.util.*

class ProjectDataSourceImpl(
    private val csvReader: CsvReader<Project>,
    private val csvWriter: CsvWriter<Project>,
    private val fileName: String = FileName.PROJECTS

) : ProjectDataSource {
    override suspend fun createProject(project: Project) {
        buildSuccessCreate(project)
    }


    override suspend fun editProject(project: Project) {
        buildSuccessEditor(project)
    }


    override suspend fun deleteProject(id: UUID) {
        buildSuccessDelete(id)
    }

    override suspend fun getAllProjects(): List<Project> {
        return csvReader.read(fileName).ifEmpty { throw FileNotFoundException() }
    }


    override suspend fun getProject(id: UUID): Project {
        return csvReader
            .read(fileName)
            .find { it.id == id }
            ?: throw ProjectNotFoundException()
    }

    override suspend fun addState(projectId: UUID, taskState: TaskState): Project {
        return modifyProjectState(projectId) { states ->
            if (states.any { oldState ->
                    haveSameStateName(
                        oldTaskState = oldState,
                        newTaskState = taskState
                    )
                }) throw DuplicateStateException()
            states + taskState
        }
    }


    override suspend fun editState(projectId: UUID, taskState: TaskState): Project {
        return modifyProjectState(projectId) { states ->
            val updatedTaskStates = mutableListOf<TaskState>()
            var notFoundState = true
            states.forEach { oldState ->
                if (haveSameStateName(oldState, taskState)) throw DuplicateStateException()
                updatedTaskStates += if (oldState.id == taskState.id) {
                    notFoundState = false
                    taskState
                } else oldState
            }
            if (notFoundState) throw StateNotFoundException()
            updatedTaskStates
        }
    }

    override suspend fun deleteState(projectId: UUID, taskState: TaskState): Project {
        return modifyProjectState(projectId) { states ->
            val updatedTaskStates = mutableListOf<TaskState>()
            var notFoundState = true
            states.forEach { oldState ->
                if (oldState.id == taskState.id) {
                    notFoundState = false
                } else updatedTaskStates += oldState
            }
            if (notFoundState) throw StateNotFoundException()
            updatedTaskStates
        }
    }

    override suspend fun addMate(projectId: UUID, user: User): Project {
        return modifyProjectUser(projectId) { users ->
            val updatedUser = mutableListOf<User>()
            var notFoundUser = true
            users.forEach { oldUsers ->
                if (haveSameUserID(oldUser = oldUsers, newUser = user))
                    throw DuplicateStateException()
                updatedUser += if (oldUsers.id == user.id) {
                    notFoundUser = false
                    user
                } else
                    oldUsers
            }
            if (notFoundUser)
                throw StateNotFoundException()
            updatedUser
        }
    }

    override suspend fun getUserProjectsById(userId: UUID): List<Project> {
        val project = csvReader
            .read(fileName)
            .filter { it.creatorUserID == userId }
        return project.ifEmpty { throw ProjectNotFoundException() }
    }


    private suspend fun modifyProjectUser(
        projectId: UUID,
        userModifier: (List<User>) -> List<User>
    ): Project {
        val projects = getAllProjects()

        val (updatedProjects, updatedProject) = findAndUpdateProject(
            projects = projects,
            projectId = projectId
        ) { project ->
            project.copy(
                users = userModifier(project.users),
                updatedAt = LocalDateTime.now()
            )
        }

        csvWriter.writeToFile(updatedProjects, fileName)
        return updatedProject ?: throw ProjectNotFoundException()
    }


    private suspend fun modifyProjectState(
        projectId: UUID,
        taskStateModifier: (List<TaskState>) -> List<TaskState>
    ): Project {
        val projects = getAllProjects()
        val (updatedProject, project) = findAndUpdateProject(
            projects = projects,
            projectId = projectId
        ) { project ->
            project.copy(
                taskStates = taskStateModifier(project.taskStates),
                updatedAt = LocalDateTime.now()
            )
        }

        csvWriter.writeToFile(updatedProject, fileName)

        return project ?: throw ProjectNotFoundException()
    }


    private fun findAndUpdateProject(
        projects: List<Project>,
        projectId: UUID,
        projectModifier: (Project) -> Project
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

    private fun haveSameStateName(
        oldTaskState: TaskState,
        newTaskState: TaskState
    ): Boolean {
        return oldTaskState.name.equals(newTaskState.name, ignoreCase = true)
    }

    private fun haveSameUserID(
        oldUser: User,
        newUser: User
    ): Boolean {
        return oldUser.id == newUser.id
    }

    private suspend fun buildSuccessCreate(project: Project) {
        val existingProjects = getAllProjects()
        if (existingProjects.any
            {
                it.title == project.title
                && it.creatorUserID == project.creatorUserID
            }
        ) {
            throw ProjectCreationFailedException()
        }
        csvWriter.writeToFile(existingProjects + project, fileName)
    }

    private suspend fun buildSuccessEditor(project: Project) {
        val existingProjects = getAllProjects()
        val index = existingProjects.indexOfFirst { it.id == project.id }
        if (index == -1) {
            throw ProjectEditFailedException()
        }
        val updatedProjects = existingProjects.toMutableList().apply {
            this[index] = project.copy(updatedAt = LocalDateTime.now())
        }
        csvWriter.writeToFile(updatedProjects, fileName)
    }

    private suspend fun buildSuccessDelete(id: UUID) {
        val existingProjects = getAllProjects()
        if (existingProjects.none { it.id == id }) {
            throw ProjectDeletionFailedException()
        }
        val updatedProjects = existingProjects.filterNot { it.id == id }
        csvWriter.writeToFile(updatedProjects, fileName)
    }
}