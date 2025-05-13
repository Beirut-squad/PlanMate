package domain.use_case.project

import creator_helper.createProjectHelper
import ui.common.exception.EmptyStateNameException
import domain.model.State
import domain.repository.ProjectRepository
import domain.use_case.log.CreateProjectLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals

class EditProjectStateUseCaseTest {

 private lateinit var repository: ProjectRepository
 private lateinit var logUseCase: CreateProjectLogUseCase
 private lateinit var useCase: EditProjectStateUseCase

 @BeforeEach
 fun setup() {
  repository = mockk()
  logUseCase = mockk()
  useCase = EditProjectStateUseCase(repository, logUseCase)
 }

 @Test
 fun `should edit state in project and log the change`() = runTest {
  // Given
  val userId = UUID.randomUUID()
  val project = createProjectHelper()
  val state = State(UUID.randomUUID(), "In Progress")
  val updatedProject = project.copy(states = listOf(state))

  coEvery { repository.editStateToProject(project.id, state) } returns updatedProject
  coEvery { logUseCase.createProjectLog(userId, project, updatedProject) } just Runs

  // when
  val result = useCase.editStateToProject(userId, project, state)

  // then
  assertEquals(updatedProject, result)
  coVerify { repository.editStateToProject(project.id, state) }
  coVerify { logUseCase.createProjectLog(userId, project, updatedProject) }
 }

 @Test
 fun `should throw EmptyStateNameException when state name is blank`() = runTest {
  // Given
  val userId = UUID.randomUUID()
  val project = createProjectHelper()
  val blankState = State(UUID.randomUUID(), " ")

  //when & then
  assertFailsWith<EmptyStateNameException> {
   useCase.editStateToProject(userId, project, blankState)
  }
  coVerify(exactly = 0) { repository.editStateToProject(any(), any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }


}
