package logic.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.project_magement_exceptions.EmptyProjectNameException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.*

class EditProjectNameUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    fun editProject(project: Project, newName: String?, editorUserId: UUID) {
        if (newName.isNullOrBlank()) { throw EmptyProjectNameException() }
        if (project.name == newName) { return }
        val editedProject = project.copy(
            name = newName
        )
        projectRepository.editProject(editedProject)
        logUseCase.createProjectLog(
            previousProject = project,
            currentProject = editedProject,
            userId = editorUserId
        )

    }
}


