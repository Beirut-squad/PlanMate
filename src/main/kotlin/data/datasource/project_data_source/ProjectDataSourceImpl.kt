package org.example.data.datasource.project_data_source

import FileName
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.*

class ProjectDataSourceImpl(
    private val writer: CsvWriter<Project>,
    private val reader: CsvReader<Project>,
) : ProjectDataSource {
    private val fileName = FileName.PROJECTS_FILE

    override fun createProject() {
        TODO("Not yet implemented")
    }

    override fun editProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun getAllProjects(): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun getProject(id: UUID): Result<Project> {
        return runCatching {
            reader.read(fileName).find { it.id == id }
                ?: throw NoProjectFoundException()
        }
    }

    //region Test cases for addStateToProject()
    override fun addStateToProject(projectId: UUID, state: State): Result<Unit> {
        return getAllProjects()
            .mapCatching { projects ->
                val updatedProjects = addStateToProjectInList(projects, projectId, state)
                writer.writeToFile(updatedProjects, fileName)
            }
    }

    private fun addStateToProjectInList(projects: List<Project>, projectId: UUID, newState: State): List<Project> {
        val updated = mutableListOf<Project>()
        var projectFound = false
        for (project in projects) {
            if (project.id == projectId) {
                validateStateNotDuplicate(newState, project.state)
                projectFound = true
                updated += project.copy(
                    state = project.state + newState,
                    updatedAt = LocalDateTime.now()
                )
            } else {
                updated += project
            }
        }
        if (!projectFound) throw NoProjectFoundException()
        return updated
    }

    private fun validateStateNotDuplicate(newState: State, existingStates: List<State>) {
        if (hasDuplicateState(newState, existingStates)) throw DuplicateStateException()
    }

    private fun hasDuplicateState(newState: State, existingStates: List<State>): Boolean {
        return existingStates.any { it.name.lowercase() == newState.name.lowercase() }
    }
    //endregion

    override fun editStateToProject(projectId: UUID, state: State): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun removeStateFromProject(projectId: UUID, state: State): Result<Unit> {
        TODO("Not yet implemented")
    }
}