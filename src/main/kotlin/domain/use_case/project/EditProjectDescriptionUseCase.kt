package domain.use_case.project

import domain.exception.EmptyProjectDescriptionException
import domain.model.Project
import domain.use_case.log.CreateProjectLogUseCase
import org.example.domain.repository.ProjectRepository
import java.time.LocalDateTime
import java.util.*

class EditProjectDescriptionUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun editProject(project: Project, newDescription: String?, editorUserId: UUID) {
        if (newDescription.isNullOrBlank()) { throw EmptyProjectDescriptionException() }
        if (project.description == newDescription) { return }
        val editedProject = project.copy(
            description = newDescription,
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
