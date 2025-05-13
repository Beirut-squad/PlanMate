package domain.use_case.project

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import domain.exception.EmptyStateNameException
import domain.repository.ProjectRepository
import domain.use_case.log.CreateProjectLogUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import java.util.*

class DeleteProjectStateUseCaseTest {

 private lateinit var repository: ProjectRepository
 private lateinit var logUseCase: CreateProjectLogUseCase
 private lateinit var useCase: DeleteProjectStateUseCase

 @BeforeTest
 fun setUp() {
  repository = mockk(relaxed = true)
  logUseCase = mockk(relaxed = true)
  useCase = DeleteProjectStateUseCase(repository, logUseCase)
 }

 @Test
 fun `removeStateFromProject should remove state and log correctly`() = runTest {
  // Given
  val userId = UUID.randomUUID()
  val stateToRemove = createStateHelper(name = "To Do")
  val originalProject = createProjectHelper(state = listOf(stateToRemove))
  val updatedProject = originalProject.copy(states = emptyList())

  coEvery { repository.removeStateFromProject(originalProject.id, stateToRemove) } returns updatedProject

  // When
  val result = useCase.removeStateFromProject(userId, originalProject, stateToRemove)

  // Then
  assert(result.states.isEmpty())
  coVerify(exactly = 1) { repository.removeStateFromProject(originalProject.id, stateToRemove) }
  coVerify(exactly = 1) {
   logUseCase.createProjectLog(userId, previousProject = originalProject, currentProject = updatedProject)
  }
 }

 @Test
 fun `removeStateFromProject should throw EmptyStateNameException when state name is blank`() = runTest {
  // Given
  val userId = UUID.randomUUID()
  val blankState = createStateHelper(name = "   ")
  val project = createProjectHelper()

  // When & Then
  assertFailsWith<EmptyStateNameException> {
   useCase.removeStateFromProject(userId, project, blankState)
  }

  coVerify(exactly = 0) { repository.removeStateFromProject(any(), any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }
}
