package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.*
import kotlin.collections.List

class GetProjectForMateByUserIdUseCase(
    private val repository: ProjectRepository,
) {
    fun getProjectForMateByUserId(userId: UUID): Result<List<Project>> {
        return repository.getAllProjects().fold(
               onSuccess = {   projects ->
                   Result.success(projects.filter { it.users.equals(userId) })
                           },

               onFailure = {
                    Result.failure(it)
               }
           )


    }
}