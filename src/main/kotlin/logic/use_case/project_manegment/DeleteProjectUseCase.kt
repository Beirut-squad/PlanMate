package org.example.logic.use_case.project_manegment

import org.example.logic.exceptions.ProjectNotDeletedException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.*

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    //private val logUseCase: CreateProjectLogUseCase
) {
    fun deleteProject(creatorUserID: UUID, project: Project) {
        //logUseCase.createProjectLog(creatorUserID, project, project)
        projectRepository.deleteProject(project.id)
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