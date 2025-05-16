package domain.useCase.project

import creator_helper.createProjectHelper
import ui.common.exception.EmptyStateNameException
import domain.model.TaskState
import domain.repository.ProjectRepository
import domain.useCase.log.CreateProjectLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals

class EditProjectTaskStateUseCaseTest {

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
  val taskState = TaskState(UUID.randomUUID(), "In Progress")
  val updatedProject = project.copy(taskStates = listOf(taskState))

  coEvery { repository.editStateToProject(project.id, taskState) } returns updatedProject
  coEvery { logUseCase.createProjectLog(userId, project, updatedProject) } just Runs

  // when
  val result = useCase.editStateToProject(userId, project, taskState)

  // then
  assertEquals(updatedProject, result)
  coVerify { repository.editStateToProject(project.id, taskState) }
  coVerify { logUseCase.createProjectLog(userId, project, updatedProject) }
 }

 @Test
 fun `should throw EmptyStateNameException when state name is blank`() = runTest {
  // Given
  val userId = UUID.randomUUID()
  val project = createProjectHelper()
  val blankTaskState = TaskState(UUID.randomUUID(), " ")

  //when & then
  assertFailsWith<EmptyStateNameException> {
   useCase.editStateToProject(userId, project, blankTaskState)
  }
  coVerify(exactly = 0) { repository.editStateToProject(any(), any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }


}
