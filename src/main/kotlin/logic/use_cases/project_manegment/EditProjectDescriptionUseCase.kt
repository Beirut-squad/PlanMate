package logic.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.project_magement_exceptions.EmptyProjectDescriptionException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.time.LocalDateTime
import java.util.*

class EditProjectDescriptionUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    fun editProject(project: Project, newDescription: String?, editorUserId: UUID) {
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
