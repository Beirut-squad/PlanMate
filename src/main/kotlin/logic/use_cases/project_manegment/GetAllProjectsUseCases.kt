package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.example.models.Project

class GetAllProjectsUseCases(
    private val projectRepository: ProjectRepository
) {
    fun getAllProjects(): Result<List<Project>> {
        return projectRepository.getAllProjects()
    }
}