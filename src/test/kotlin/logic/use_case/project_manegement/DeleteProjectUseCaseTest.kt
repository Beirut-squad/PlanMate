package logic.use_case.project_manegement

import io.mockk.mockk
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegement.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach

class DeleteProjectUseCaseTest {
    private val projectUseCase: ProjectRepository = mockk()
    private lateinit var deleteProjectUseCaseTest: DeleteProjectUseCase

    @BeforeEach
    fun setup(){
        deleteProjectUseCaseTest = DeleteProjectUseCase(projectUseCase)
    }
}