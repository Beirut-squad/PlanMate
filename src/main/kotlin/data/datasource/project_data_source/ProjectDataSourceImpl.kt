package org.example.data.datasource.project_data_source

import FileName
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.exceptions.NoStateException
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

    fun modifyProjectState(projectId: UUID, stateModifier: (List<State>) -> List<State>): Result<Unit> {
        return getAllProjects().mapCatching { projects ->
            val updatedProjects = findAndUpdateProject(projects, projectId) { project ->
                project.copy(
                    state = stateModifier(project.state),
                    updatedAt = LocalDateTime.now()
                )
            }
            writer.writeToFile(updatedProjects, fileName)
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
}