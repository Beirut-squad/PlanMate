package org.example.logic.use_case.project_manegement

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project

class DeleteProjectUseCase (private val projectRepository: ProjectRepository) {
    fun deleteProject(){}
}