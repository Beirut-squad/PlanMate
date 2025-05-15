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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFailsWith

class AddProjectTaskStateUseCaseTest{

  private lateinit var repository: ProjectRepository
  private lateinit var logUseCase: CreateProjectLogUseCase
  private lateinit var useCase: AddProjectStateUseCase

  @BeforeEach
  fun setUp() {
   repository = mockk(relaxed = true)
   logUseCase = mockk(relaxed = true)
   useCase = AddProjectStateUseCase(repository, logUseCase)
  }

  @Test
  fun `Given valid state, When adding state to project, Then project is updated and log is created`() = runTest {
   // Given
   val userId = UUID.randomUUID()
   val project = createProjectHelper()
   val state = createStateHelper(name = "In Progress")
   val updatedProject = project.copy(taskStates = project.taskStates + state)

   coEvery { repository.addStateToProject(project.id, state) } returns updatedProject

   // When
   val result = useCase.addStateToProject(userId, project, state)

   // Then
   assertEquals(updatedProject, result)
   coVerify (exactly = 1) { repository.addStateToProject(project.id, state) }
   coVerify(exactly = 1) {
    logUseCase.createProjectLog(
     userId = userId,
     previousProject = project,
     currentProject = updatedProject
    )
   }
  }

  @Test
  fun `Given empty state name, When adding state to project, Then EmptyStateNameException is thrown`() = runTest {
   // Given
   val userId = UUID.randomUUID()
   val project = createProjectHelper()
   val invalidState = createStateHelper(name = "   ") // blank name

   // When & Then
   assertFailsWith<EmptyStateNameException> {
    useCase.addStateToProject(userId, project, invalidState)
   }

   coVerify(exactly = 0) { repository.addStateToProject(any(), any()) }
   coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
  }
 }