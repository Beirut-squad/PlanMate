package domain.useCase.project

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import ui.common.exception.EmptyStateNameException
import domain.repository.ProjectRepository
import domain.useCase.log.CreateProjectLogUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import java.util.*

class DeleteProjectTaskStateUseCaseTest {

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
  val originalProject = createProjectHelper(taskState = listOf(stateToRemove))
  val updatedProject = originalProject.copy(taskStates = emptyList())

  coEvery { repository.deleteStateFromProject(originalProject.id, stateToRemove) } returns updatedProject

  // When
  val result = useCase.removeStateFromProject(userId, originalProject, stateToRemove)

  // Then
  assert(result.taskStates.isEmpty())
  coVerify(exactly = 1) { repository.deleteStateFromProject(originalProject.id, stateToRemove) }
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

  coVerify(exactly = 0) { repository.deleteStateFromProject(any(), any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }
}
