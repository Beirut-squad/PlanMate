package domain.useCase.project

import creator_helper.createProjectHelper
import domain.repository.ProjectRepository
import domain.useCase.log.CreateProjectLogUseCase
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DeleteProjectUseCaseTest {

 private lateinit var projectRepository: ProjectRepository
 private lateinit var logUseCase: CreateProjectLogUseCase
 private lateinit var deleteProjectUseCase: DeleteProjectUseCase

 @BeforeTest
 fun setUp() {
  projectRepository = mockk(relaxed = true)
  logUseCase = mockk(relaxed = true)
  deleteProjectUseCase = DeleteProjectUseCase(projectRepository, logUseCase)
 }

@Test
 fun `deleteProject should delete project and log the deletion`() = runTest {
  // Given
  val project = createProjectHelper()

  // When
  deleteProjectUseCase.deleteProject(project.creatorUserID, project)

  // Then
  coVerify(exactly = 1) { projectRepository.deleteProject(project.id) }
  coVerify(exactly = 1) { logUseCase.createProjectLog(project.creatorUserID, project, null) }
 }
}
