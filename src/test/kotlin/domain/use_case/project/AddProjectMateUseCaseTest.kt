package domain.use_case.project

import creator_helper.createProjectHelper
import creator_helper.createUserHelper
import ui.common.exception.ProjectNotFoundException
import domain.repository.ProjectRepository
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.log.CreateProjectLogUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AddProjectMateUseCaseTest {
 private lateinit var repository: ProjectRepository
 private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
 private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
 private lateinit var logUseCase: CreateProjectLogUseCase
 private lateinit var useCase: AddProjectMateUseCase

 @BeforeEach
 fun setUp() {
  repository = mockk(relaxed = true)
  getProjectByIdUseCase = mockk()
  getCurrentUserUseCase = mockk()
  logUseCase = mockk(relaxed = true)
  useCase = AddProjectMateUseCase(repository, getProjectByIdUseCase, getCurrentUserUseCase, logUseCase)
 }


@Test
fun `addMateToProject should succeed and call all dependencies correctly`() = runTest {
 // Given
 val projectId = UUID.randomUUID()
 val userToAdd = createUserHelper()
 val previousProject = createProjectHelper(id = projectId)
 val updatedProject = previousProject.copy(users = previousProject.users + userToAdd)
 val currentUser = createUserHelper()

 coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns previousProject
 coEvery { getCurrentUserUseCase.getCurrentUser() } returns currentUser
 coEvery { repository.addMateToProject(projectId, userToAdd) } returns updatedProject

 // When
 useCase.addMateToProject(projectId, userToAdd)

 // Then
 coVerify(exactly = 1) { getProjectByIdUseCase.getProjectById(projectId) }
 coVerify(exactly = 1) { getCurrentUserUseCase.getCurrentUser() }
 coVerify(exactly = 1) { repository.addMateToProject(projectId, userToAdd) }
 coVerify(exactly = 1) {
  logUseCase.createProjectLog(
   currentUser.id,
   previousProject = previousProject,
   currentProject = updatedProject
  )
 }
}
 @Test
 fun `should throw ProjectNotFoundException when project does not exist`() = runTest {
  val projectId = UUID.randomUUID()
  val user = createUserHelper()

  coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws ProjectNotFoundException()

  assertFailsWith<ProjectNotFoundException> {
   useCase.addMateToProject(projectId, user)
  }
 }
///////
@Test
fun `addMateToProject should throw exception when current user not found`() = runTest {
 // Given
 val projectId = UUID.randomUUID()
 val userToAdd = createUserHelper()
 val previousProject = createProjectHelper(id = projectId)

 coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns previousProject
 coEvery { getCurrentUserUseCase.getCurrentUser() } throws IllegalStateException("No user found")

 // When + Then
 val exception = assertFailsWith<IllegalStateException> {
  useCase.addMateToProject(projectId, userToAdd)
 }

 assertEquals("No user found", exception.message)
 coVerify(exactly = 1) { getProjectByIdUseCase.getProjectById(projectId) }
 coVerify(exactly = 1) { getCurrentUserUseCase.getCurrentUser() }
 coVerify(exactly = 0) { repository.addMateToProject(any(), any()) }
 coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
}

 @Test
 fun `addMateToProject should fail if repository throws exception`() = runTest {
  val projectId = UUID.randomUUID()
  val user = createUserHelper()
  val previousProject = createProjectHelper(id = projectId)
  val currentUser = createUserHelper()

  coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns previousProject
  coEvery { getCurrentUserUseCase.getCurrentUser() } returns currentUser
  coEvery { repository.addMateToProject(projectId, user) } throws IllegalArgumentException("Cannot add user")

  assertFailsWith<IllegalArgumentException> {
   useCase.addMateToProject(projectId, user)
  }

  coVerify(exactly = 1) { getProjectByIdUseCase.getProjectById(projectId) }
  coVerify(exactly = 1) { getCurrentUserUseCase.getCurrentUser() }
  coVerify(exactly = 1) { repository.addMateToProject(projectId, user) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }

}