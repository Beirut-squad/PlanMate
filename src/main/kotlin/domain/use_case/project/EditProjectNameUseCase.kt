package domain.use_case.project

import data.csv.model.Project
import domain.exception.project.EmptyProjectNameException
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
        if (project.name == newName) { return }
        val editedProject = project.copy(
            name = newName,
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


