package org.example.logic.use_case.project_manegement

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.*


class EditProjectUseCase(private val projectRepository: ProjectRepository) {
    fun editProjectName(project: Project, newName: String, userId: UUID): Result<String> {
        project.name = newName
        project.updatedAt = LocalDateTime.now()
        //fun createProjectLog(userId, previousProject = project, currentProject =  project)
        return projectRepository.editProject(buildProject())
    }
    fun editProjectDescription(project: Project, newDescription: String, userId: UUID): Result<String> {
        project.description = newDescription
        project.updatedAt = LocalDateTime.now()
        //fun createProjectLog(userId, previousProject = project, currentProject =  project)
        return projectRepository.editProject(buildProject())
    }
    fun editProjectNameAndDescription(project: Project, newName: String, newDescription: String, userId: UUID): Result<String> {
        project.name = newName
        project.description = newDescription
        project.updatedAt = LocalDateTime.now()
        //fun createProjectLog(userId, previousProject = project, currentProject =  project)
        return projectRepository.editProject(buildProject())
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