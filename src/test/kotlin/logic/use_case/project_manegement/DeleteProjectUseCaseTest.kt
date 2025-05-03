package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.mockk
import io.mockk.verify
import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegment.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class DeleteProjectUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private val logUseCase: CreateProjectLogUseCase = mockk(relaxed = true)
    private var deleteProjectUseCase: DeleteProjectUseCase = mockk(relaxed = true)
    private val creatorUserID = UUID.randomUUID()

    @BeforeEach
    fun setup(){
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository,logUseCase)
    }

    @Test
    fun `should delete project successfully when call deleteProject function`(){
        //Given
        val project = createProjectHelper()
        deleteProjectUseCase.deleteProject(creatorUserID,project)

        //When &Then
        verify(exactly = 1) { logUseCase.createProjectLog(creatorUserID,project,project) }
        verify(exactly = 1) { projectRepository.deleteProject(project.id) }
    }
}