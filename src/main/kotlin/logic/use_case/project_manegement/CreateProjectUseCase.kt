package org.example.logic.use_case.project_manegement

import org.example.logic.repositories.project_repository.ProjectRepository


class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun createProject(){}
}