package org.example.logic.use_cases.project_manegment

import logic.use_cases.log.GetProjectLogsByProjectIdUseCase
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.example.models.Project
import java.util.UUID

class GetAllProjectsUseCases(
    private val projectRepository: ProjectRepository,
   private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase
) {
    suspend fun getAllProjects(): List<Project> {
        getAllProjectLogsUseCase.getAllProjectLogs()
        return projectRepository.getAllProjects()
    }
}