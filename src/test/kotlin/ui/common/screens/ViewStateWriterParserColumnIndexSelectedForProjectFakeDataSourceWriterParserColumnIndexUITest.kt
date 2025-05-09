package ui.common.screens

import io.mockk.*
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import data.csv.model.Project
import data.csv.model.State
import data.csv.model.Task
import org.example.ui.common.components.Printer
import org.example.ui.common.screens.ViewStateSelectedForProjectUI
import org.example.ui.mate.home_screen.ViewProjectsForUserUI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.BeforeTest

class ViewStateWriterParserColumnIndexSelectedForProjectFakeDataSourceWriterParserColumnIndexUITest{
  private val printer: Printer = mockk(relaxUnitFun = true)
  private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk()
  private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId = mockk()
 private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase = mockk()
 private lateinit var projectId: UUID
  private lateinit var ui: ViewStateSelectedForProjectUI

 @BeforeTest
 fun setUp(){
  projectId = UUID.randomUUID()
  startKoin() {
   modules(module {
    single<Printer> { printer }
    single<GetProjectByIdUseCase> { getProjectByIdUseCase }
    single<GetTaskByStateIdAndProjectId> { getTaskByStateIdAndProjectId }
    single { getCurrentLoggedInUserUseCase }
   })
  }
  every { getCurrentLoggedInUserUseCase.getCurrentUser() } returns mockk()
  ui = ViewStateSelectedForProjectUI(projectId)
  }

@AfterEach
fun tearDown(){
 stopKoin()
}

 @Test
 fun `should navigate to ViewProjectsForUserUI when no states available for the project`() {
  // Given
  val project = Project(
   id = projectId,
   name = "No States Project",
   description = "No states available",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = emptyList()
  )
  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { printer.readIntInput("Enter the number of the state to view (Enter Any Thing To Go Back): ") } returns 999

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  ui.show()

  verify { anyConstructed<ViewProjectsForUserUI>().show() }
 }

 @Test
 fun `should print error when project retrieval failed`() {
  // Given
  val error = RuntimeException("Something went wrong")

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.failure(error)

  ui.show()

  verify { printer.printError("Failed to retrieve project: Something went wrong") }
 }

 @Test
 fun `should print state options when states are available for the project`() {
  // Given
  val task = Task(
   id = UUID.randomUUID(),
   projectId = UUID.randomUUID(),
   name = "Test Task",
   description = "Task for testing state options",
   state = State(UUID.randomUUID(), "To Do"),
   creatorUserID = UUID.randomUUID(),
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now()
  )

  val state = State(UUID.randomUUID(), "To Do")
  val project = Project(
   id = projectId,
   name = "Project with States",
   description = "Contains states",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state)
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(any(), any()) } returns Result.success(listOf(task))


  every { printer.readIntInput("Enter the number of the state to view (Enter Any Thing To Go Back): ") } returns 1

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  ui.show()

  verify { printer.printInfoLine("1. To Do") }

  verify { anyConstructed<ViewProjectsForUserUI>().show() }
 }


 @Test
 fun `should navigate to ViewProjectsForUserUI when invalid option is selected`() {
  // Given
  val state = State(UUID.randomUUID(), "To Do")
  val project = Project(
   id = projectId,
   name = "Valid Project",
   description = "Can create task",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state)
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { printer.readIntInput("Enter the number of the state to view (Enter Any Thing To Go Back): ") } returns null

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { anyConstructed<ViewProjectsForUserUI>().show() }
 }

 @Test
 fun `should navigate to ViewProjectsForUserUI when no states are available`() {
  // Given
  val project = Project(
   id = projectId,
   name = "Valid Project",
   description = "Can create task",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = emptyList()
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { printer.readIntInput("Enter the number of the state to view (Enter Any Thing To Go Back): ") } returns null


  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { anyConstructed<ViewProjectsForUserUI>().show() }
 }
 @Test
 fun `should display tasks when tasks are retrieved successfully`() {
  // Given
  val state = State(UUID.randomUUID(), "To Do")
  val task = Task(
   id = UUID.randomUUID(),
   projectId = UUID.randomUUID(),
   name = "Task 1",
   description = "Task description",
   state = State(state.id, "To Do"),
   creatorUserID = UUID.randomUUID(),
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now()
  )

  val project = Project(
   id = projectId,
   name = "Valid Project",
   description = "Can create task",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state)
  )

  val result = Result.success(listOf(task))

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)


  every { getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, state.id) } returns result
  every { printer.readIntInput(any()) } returns 1

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { printer.printInfoLine("Tasks:") }
  verify { printer.printInfoLine(" - Name: Task 1, Description: Task description") }
  verify(exactly = 0) { printer.printInfoLine("No tasks available for this state.") }
 }

 @Test
 fun `should display 'No tasks available for this state' when no tasks for selected state`() {
  // Given
  val state = State(UUID.randomUUID(), "To Do")
  val project = Project(
   id = projectId,
   name = "Project with No Tasks in State",
   description = "There are no tasks in the selected state",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state)
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, state.id) } returns Result.success(emptyList())

  every { printer.readIntInput(any()) } returns 1

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { printer.printInfoLine("No tasks available for this state.") }
 }


 @Test
 fun `should return to previous UI when user chooses to go back`() {
  // Given
  val state = State(UUID.randomUUID(), "In Progress")
  val project = Project(
   id = projectId,
   name = "Valid Project",
   description = "Can create task",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state)
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { printer.readIntInput("Enter the number of the state to view (Enter Any Thing To Go Back): ") } returns 999

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { anyConstructed<ViewProjectsForUserUI>().show() }
 }

 @Test
 fun `should print error when tasks retrieval fails`() {
  // Given
  val state = State(UUID.randomUUID(), "To Do")
  val project = Project(
   id = projectId,
   name = "Valid Project",
   description = "Can create task",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state)
  )

  val error = RuntimeException("Failed to retrieve tasks")

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, state.id) } returns Result.failure(error)

  every { printer.readIntInput(any()) } returns 1

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { printer.printError("Failed to retrieve tasks: Failed to retrieve tasks") }
 }



 @Test
 fun `should go back to previous screen when user inputs non-numeric value`() {
  // Given
  val state = State(UUID.randomUUID(), "To Do")
  val project = Project(
   id = projectId,
   name = "Valid Project",
   description = "Can create task",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state)
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  every { printer.readIntInput("Enter the number of the state to view (Enter Any Thing To Go Back): ") } returns -1

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { anyConstructed<ViewProjectsForUserUI>().show() }
 }



 @Test
 fun `should display multiple states and navigate correctly based on user input`() {
  // Given
  val state1 = State(UUID.randomUUID(), "To Do")
  val state2 = State(UUID.randomUUID(), "In Progress")
  val project = Project(
   id = projectId,
   name = "Project with Multiple States",
   description = "Contains multiple states",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   creatorUserID = UUID.randomUUID(),
   state = listOf(state1, state2)
  )

  every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)

  val task1 = Task(UUID.randomUUID(), projectId, "Task 1", "Description 1", state1, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now())
  val task2 = Task(UUID.randomUUID(), projectId, "Task 2", "Description 2", state2, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now())

  every { getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, state1.id) } returns Result.success(listOf(task1))
  every { getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, state2.id) } returns Result.success(listOf(task2))

  every { printer.readIntInput(any()) } returns 1

  mockkConstructor(ViewProjectsForUserUI::class)
  every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

  val ui = ViewStateSelectedForProjectUI(projectId)
  ui.show()

  verify { printer.printInfoLine("1. To Do") }
  verify { printer.printInfoLine("2. In Progress") }
  verify { printer.printInfoLine("Tasks:") }
  verify { printer.printInfoLine(" - Name: Task 1, Description: Description 1") }
 }


}

