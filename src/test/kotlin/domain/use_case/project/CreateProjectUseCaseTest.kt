package domain.use_case.project

import creator_helper.createUserHelper
import domain.exception.EmptyProjectDescriptionException
import domain.exception.EmptyProjectTitleException
import domain.model.Project
import domain.model.State
import domain.model.User
import domain.repository.ProjectRepository
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.log.CreateProjectLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFailsWith

class CreateProjectUseCaseTest {

 private lateinit var projectRepository: ProjectRepository
 private lateinit var logUseCase: CreateProjectLogUseCase
 private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
 private lateinit var createProjectUseCase: CreateProjectUseCase

 private val userId = UUID.randomUUID()
 private val user = createUserHelper()

 @BeforeEach
 fun setUp() {
  projectRepository = mockk(relaxed = true)
  logUseCase = mockk(relaxed = true)
  getCurrentUserUseCase = mockk()
  createProjectUseCase = CreateProjectUseCase(projectRepository, logUseCase, getCurrentUserUseCase)

  coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
 }

 @Test
 fun `GIVEN valid input WHEN createProject THEN project is created and logged`() = runTest {
  // Given
  val name = "My Project"
  val description = "Some description"
  val stateNames = listOf("To Do", "In Progress")

  // When
  createProjectUseCase.createProject(name, description, stateNames)

  // Then
  coVerify(exactly = 1) { projectRepository.createProject(withArg {
   assert(it.title == name)
   assert(it.description == description)
   assert(it.states.size == 2)
   assert(it.creatorUserID == userId)
  }) }

  coVerify(exactly = 1) { logUseCase.createProjectLog(userId, previousProject = null, any()) }
 }

 @Test
 fun `GIVEN blank name WHEN createProject THEN throw EmptyProjectTitleException`() = runTest {
  // Given
  val name = "   "
  val description = "Valid"
  val states = listOf("To Do")

  // Then
  assertFailsWith<EmptyProjectTitleException> {
   // When
   createProjectUseCase.createProject(name, description, states)
  }

  coVerify(exactly = 0) { projectRepository.createProject(any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }

 @Test
 fun `GIVEN blank description WHEN createProject THEN throw EmptyProjectDescriptionException`() = runTest {
  // Given
  val name = "Valid"
  val description = "   "
  val states = listOf("Done")

  // Then
  assertFailsWith<EmptyProjectDescriptionException> {
   // When
   createProjectUseCase.createProject(name, description, states)
  }

  coVerify(exactly = 0) { projectRepository.createProject(any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }
}
