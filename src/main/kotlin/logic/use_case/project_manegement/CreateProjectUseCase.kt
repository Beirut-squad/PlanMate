package org.example.logic.use_case.project_manegement

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun createProject(creatorUserID: UUID, name: String, description: String, stateNames: List<String>) {
        val project = buildProject(name, description, stateNames)
        projectRepository.createProject(project)
     // createProjectLog(userId, previousProject = null, currentProject = project)
    }

    private fun buildProject(name: String, description: String, stateNames: List<String>): Project {
        return Project(
            id = UUID.randomUUID(),
            name = name,
            description = description,
            state = stateNames.map { State(id = UUID.randomUUID(), name = it) },
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
