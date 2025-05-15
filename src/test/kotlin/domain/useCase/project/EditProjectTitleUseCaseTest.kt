package domain.useCase.project

import creator_helper.createProjectHelper
import ui.common.exception.DuplicateTitleException
import ui.common.exception.EmptyProjectTitleException
import domain.repository.ProjectRepository
import domain.useCase.log.CreateProjectLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals

class EditProjectTitleUseCaseTest {

 private lateinit var repository: ProjectRepository
 private lateinit var logUseCase: CreateProjectLogUseCase
 private lateinit var useCase: EditProjectTitleUseCase

 @BeforeEach
 fun setup() {
  repository = mockk()
  logUseCase = mockk()
  useCase = EditProjectTitleUseCase(repository, logUseCase)
 }

 @Test
 fun `should throw exception when new title is blank`() = runTest {
  //Given
  val userId = UUID.randomUUID()
  val project = createProjectHelper()

  //when && then
  assertFailsWith<EmptyProjectTitleException> {
   useCase.editProject(project, "  ", userId)
  }

  coVerify(exactly = 0) { repository.editProject(any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }

 @Test
 fun `should throw exception when new title is null`() = runTest {
  //Given
  val userId = UUID.randomUUID()
  val project = createProjectHelper()

  //when && then
  assertFailsWith<EmptyProjectTitleException> {
   useCase.editProject(project, null, userId)
  }

  coVerify(exactly = 0) { repository.editProject(any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }

 @Test
 fun `should throw DuplicateTitleException if description is the same`() = runTest {
  //Given
  val userId = UUID.randomUUID()
  val project = createProjectHelper()
  //when && then
  assertFailsWith<DuplicateTitleException> {
   useCase.editProject(project, project.title, userId)
  }

  coVerify(exactly = 0) { repository.editProject(any()) }
  coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
 }


 @Test
 fun `should update project title and log change`() = runTest {
  //Given
  val userId = UUID.randomUUID()
  val project = createProjectHelper()
  val newTitle = "Updated Title"
   project.copy(title = newTitle)

  //when
  coEvery { repository.editProject(any()) } just Runs
  coEvery { logUseCase.createProjectLog(userId, project, any()) } just Runs

  useCase.editProject(project, newTitle, userId)
  //then
  coVerify {
   repository.editProject(withArg { assertEquals(newTitle, it.title) })
   logUseCase.createProjectLog(userId, project, withArg { assertEquals(newTitle, it.title) })
  }
 }

}
