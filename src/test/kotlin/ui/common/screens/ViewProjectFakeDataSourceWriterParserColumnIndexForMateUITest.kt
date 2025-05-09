package ui.common.screens

import io.mockk.*
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.data.model.Project
import org.example.data.model.State
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Printer
import org.example.ui.common.screens.CreateNewTaskUI
import org.example.ui.common.screens.ViewAllTaskForProjectUI
import org.example.ui.common.screens.ViewProjectForMateUI
import org.example.ui.common.screens.ViewStateSelectedForProjectUI
import org.example.ui.mate.home_screen.ViewProjectsForUserUI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.time.LocalDateTime
import java.util.UUID

class ViewProjectFakeDataSourceWriterParserColumnIndexForMateUITest{
  private val printer : Printer = mockk(relaxUnitFun = true)
  private val reader : Reader = mockk()
  private val getProjectByIdUseCase :GetProjectByIdUseCase = mockk()
 private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase = mockk()
  private lateinit var projectId: UUID
  private lateinit var ui: ViewProjectForMateUI

  @BeforeEach
  fun setup(){
   projectId = UUID.randomUUID()
   startKoin() {
    modules(module {
     single<Printer> { printer }
     single<Reader> { reader }
     single<GetProjectByIdUseCase> { getProjectByIdUseCase }
     single { getCurrentLoggedInUserUseCase }
    })
   }
   every { getCurrentLoggedInUserUseCase.getCurrentUser() } returns mockk()

   ui = ViewProjectForMateUI(projectId)
  }
 @AfterEach
 fun tearDown() {
  stopKoin()
 }

 @Test
 fun `should print error when project retrieval failed`(){
 //Given
  val error = RuntimeException("Something went wrong")

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.failure(error)

  ui.show()

  verify {
   printer.printError("Failed to retrieve project: Something went wrong")  }
 }

 @Test
 fun `should print project info and goodbye when invalid option is selected`() {
  val project = Project(
   id = projectId,
   title = "Test Project",
   description = "Testing project",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(State(UUID.randomUUID(), "To Do"))
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
  every { reader.readInt() } returns 999

  ui.show()

  verify {
   printer.printInfoLine(match { it.contains("Project Name: Test Project") })
   printer.printOptions(any(), any(), any(), any())
   printer.printGoodbyeMessage("Goodbye")
  }
 }

 @Test
 fun `should print error when trying to create task but project has no states`() {
  val project = Project(
   id = projectId,
   title = "No State Project",
   description = "No states available",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = emptyList()
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
  every { reader.readInt() } returns 3

  ui.show()

  verify {
   printer.printError("Cannot create a task because this project has no states.")
  }
 }

 @Test
 fun `should navigate to ViewStateSelectedForProjectUI when option 1 is selected`() {
  val project = Project(
   id = projectId,
   title = "With State",
   description = "Project with states",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(State(UUID.randomUUID(), "To Do"))
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
  every { reader.readInt() } returns 1

  mockkConstructor(ViewStateSelectedForProjectUI::class)
  every { anyConstructed<ViewStateSelectedForProjectUI>().show() } just Runs

  ui.show()

  verify { anyConstructed<ViewStateSelectedForProjectUI>().show() }
 }
 @Test
 fun `should navigate to ViewAllTaskForProjectUI when option 2 is selected`() {
  val project = Project(
   id = projectId,
   title = "With Tasks",
   description = "Project with tasks",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(State(UUID.randomUUID(), "To Do"))
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
  every { reader.readInt() } returns 2

  mockkConstructor(ViewAllTaskForProjectUI::class)
  every { anyConstructed<ViewAllTaskForProjectUI>().show() } just Runs

  ui.show()

  verify { anyConstructed<ViewAllTaskForProjectUI>().show() }
 }

 @Test
 fun `should navigate to CreateNewTaskUI when option 3 is selected and project has states`() {
  val project = Project(
   id = projectId,
   title = "Valid Project",
   description = "Can create task",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(State(UUID.randomUUID(), "To Do"))
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
  every { reader.readInt() } returns 3

  mockkConstructor(CreateNewTaskUI::class)
  every { anyConstructed<CreateNewTaskUI>().show() } just Runs

  ui.show()

  verify { anyConstructed<CreateNewTaskUI>().show() }
 }

 @Test
 fun `should navigate to ViewProjectsForUserUI when invalid option is selected`() {
  val project = Project(
   id = projectId,
   title = "Another Project",
   description = "With states",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(State(UUID.randomUUID(), "Doing"))
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
  every { reader.readInt() } returns 999

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  ui.show()

  verify { anyConstructed<ViewProjectsForUserUI>().show() }
 }
}