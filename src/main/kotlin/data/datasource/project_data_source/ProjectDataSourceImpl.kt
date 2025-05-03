package org.example.data.datasource.project_data_source


import data.csv.FileName
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotCreatedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotDeletedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotEditedException
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotGetAllProjectsException
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.exceptions.NoStateException
import org.example.models.Project
import org.example.models.State
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
            csvReader.read(fileName).find { it.id == id }
                ?: throw NoProjectFoundException()
        }
    }

    override fun addStateToProject(projectId: UUID, state: State): Result<Unit> {
        return modifyProjectState(projectId) { states ->
            if(states.any { oldState -> haveSameStateName(oldState, state) }) throw DuplicateStateException()
            states + state
        }
    }

    override fun editStateToProject(projectId: UUID, state: State): Result<Unit> {
        return modifyProjectState(projectId) { states ->
            val updatedStates = mutableListOf<State>()
            states.forEach { oldState ->
                if (!haveSameStateId(oldState, state)) throw NoStateException()
                if (haveSameStateName(oldState, state)) throw DuplicateStateException()
                updatedStates += if (oldState.id == state.id) state else oldState
            }
            updatedStates
        }
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Unit> {
        TODO("Not yet implemented")
    }


    private fun modifyProjectState(projectId: UUID, stateModifier: (List<State>) -> List<State>): Result<Unit> {
        return getAllProjects().mapCatching { projects ->
            val updatedProjects = findAndUpdateProject(projects, projectId) { project ->
                project.copy(
                    state = stateModifier(project.state),
                    updatedAt = LocalDateTime.now()
                )
            }
            csvWriter.writeToFile(updatedProjects, fileName)
        }
    }

    private fun findAndUpdateProject(projects: List<Project>, projectId: UUID, projectModifier: (Project) -> Project): List<Project> {
        val updated = mutableListOf<Project>()
        var projectFound = false

        for (project in projects) {
            if (project.id == projectId) {
                projectFound = true
                updated += projectModifier(project)
            } else {
                updated += project
            }
        }

        if (!projectFound) throw NoProjectFoundException()
        return updated
    }

    private fun haveSameStateName(oldState: State, newState: State): Boolean {
        return oldState.name.equals(newState.name, ignoreCase = true)
    }

    private fun haveSameStateId(oldState: State, newState: State): Boolean {
        return oldState.id == newState.id
    }
companion object {
    const val PROJECTS_FILE = "Project.csv"
}
private fun buildSuccessCreate(project: Project): Result<Unit>{
    val existingProjects = getAllProjects().getOrElse { return Result.failure(it) }
    if (existingProjects.any { it.name == project.name && it.creatorUserID == project.creatorUserID }) {
        return Result.failure(
            ProjectNotCreatedException(
                "Project '${project.name}' already exists for user ${project.creatorUserID}"
            )
        )
    }
    csvWriter.writeToFile(existingProjects + project, PROJECTS_FILE)
    return Result.success(Unit)
}

private fun buildSuccessEditor(project: Project):Result<Unit>{
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
    csvWriter.writeToFile(updatedProjects, PROJECTS_FILE)
    return Result.success(Unit)
}

private fun buildSuccessDelete(id: UUID):Result<Unit>{
    val existingProjects = getAllProjects().getOrElse { return Result.failure(it) }
    if (existingProjects.none { it.id == id }) {
        return Result.failure(
            ProjectNotDeletedException("Project with ID $id not found")
        )
    }
    val updatedProjects = existingProjects.filterNot { it.id == id }
    csvWriter.writeToFile(updatedProjects, PROJECTS_FILE)
    return Result.success(Unit)
}
}