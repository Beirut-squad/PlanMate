package domain.use_case.project

import domain.exception.DuplicateTitleException
import domain.model.Project
import domain.use_case.log.CreateProjectLogUseCase
import domain.exception.EmptyProjectTitleException
import domain.repository.ProjectRepository
import java.time.LocalDateTime
import java.util.*

class EditProjectTitleUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun editProject(project: Project, newName: String?, editorUserId: UUID) {
        if (newName.isNullOrBlank()) { throw EmptyProjectTitleException() }
        if (project.title == newName) { throw  DuplicateTitleException()}
        val editedProject = project.copy(
            title = newName,
            updatedAt = LocalDateTime.now()
        )
        projectRepository.editProject(editedProject)
        logUseCase.createProjectLog(
            previousProject = project,
            currentProject = editedProject,
            userId = editorUserId
        )

    }
}


