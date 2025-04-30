package org.example.logic.use_case.project_manegement

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.*

class DeleteProjectUseCase(private val projectRepository: ProjectRepository) {
    fun deleteProject() {

        //fun createProjectLog(userId, previousProject = null, currentProject =  project)
        projectRepository.deleteProject(buildProject())
    }
    private fun buildProject(): Project{
        return Project(
            id = UUID.randomUUID(),
            name = "Bassant",
            description = "",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            state = listOf(State(id = UUID.randomUUID(), name = ""))
        )
    }
}