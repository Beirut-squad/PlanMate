package domain.use_case.project

import domain.exception.project.EmptyProjectNameException
import domain.model.Project
import domain.use_case.log.CreateProjectLogUseCase
import org.example.domain.repository.ProjectRepository
import java.time.LocalDateTime
import java.util.*

class EditProjectNameUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun editProject(project: Project, newName: String?, editorUserId: UUID) {
        if (newName.isNullOrBlank()) { throw EmptyProjectNameException() }
        if (project.title == newName) { return }
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


