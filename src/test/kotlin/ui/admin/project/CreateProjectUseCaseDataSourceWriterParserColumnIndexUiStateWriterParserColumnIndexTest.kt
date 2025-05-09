package ui.admin.project

import io.mockk.*
import org.example.logic.use_cases.project_manegment.AddStateToProjectUseCase
import org.example.data.model.State
import domain.use_case.state.CreateStateUseCase
import org.example.ui.admin.project.CreateProjectStateUi
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Printer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.*

class CreateProjectUseCaseDataSourceWriterParserColumnIndexUiStateWriterParserColumnIndexTest {
 // Mocks
 private val mockPrinter: Printer = mockk(relaxed = true)
 private val mockReader: Reader = mockk()
 private val mockAddStateUseCase: AddStateToProjectUseCase = mockk(relaxed = true)
 private val mockCreateStateUseCase: CreateStateUseCase = mockk()

 // Test data
 private val testProjectId = UUID.randomUUID()
 private val testStateName = "In Progress"
 private val mockState: State = mockk()

 // System under test
 private lateinit var createProjectStateUi: CreateProjectStateUi

 @BeforeEach
 fun setUp() {
  startKoin {
   modules(
    module {
     single { mockPrinter }
     single { mockReader }
     single { mockAddStateUseCase }
     single { mockCreateStateUseCase }
    }
   )
  }

  createProjectStateUi = CreateProjectStateUi(testProjectId)
 }
 @AfterEach
 fun tearDown() {
  stopKoin()
 }

 @Test
 fun `show should display title and prompt for state name`() {
  // Arrange
  every { mockReader.readInput() } returns testStateName
  every { mockCreateStateUseCase.createState(testStateName) } returns mockState

  // Act
  createProjectStateUi.show()

  // Assert
  verifyOrder {
   mockPrinter.printTitle("Create Task State")
   mockPrinter.printTitle("Please, Write your state name")
   mockReader.readInput()
   mockCreateStateUseCase.createState(testStateName)
   mockAddStateUseCase.addStateToProject(projectId = testProjectId, state = mockState)
  }
 }
 @Test
 fun `show should read user input for state name`() {
  // Arrange
  every { mockReader.readInput() } returns testStateName
  every { mockCreateStateUseCase.createState(testStateName) } returns mockState

  // Act
  createProjectStateUi.show()

  // Assert
  verify { mockReader.readInput() }
 }

 @Test
 fun `show should create state with provided name`() {
  // Arrange
  every { mockReader.readInput() } returns testStateName
  every { mockCreateStateUseCase.createState(testStateName) } returns mockState

  // Act
  createProjectStateUi.show()

  // Assert
  verify { mockCreateStateUseCase.createState(testStateName) }
 }

 @Test
 fun `show should add created state to project with correct project ID`() {
  // Arrange
  every { mockReader.readInput() } returns testStateName
  every { mockCreateStateUseCase.createState(testStateName) } returns mockState

  // Act
  createProjectStateUi.show()

  // Assert
  verify { mockAddStateUseCase.addStateToProject(projectId = testProjectId, state = mockState) }
 }

 @Test
 fun `show should execute all operations in correct order`() {
  // Arrange
  every { mockReader.readInput() } returns testStateName
  every { mockCreateStateUseCase.createState(testStateName) } returns mockState

  // Act
  createProjectStateUi.show()

  // Assert
  verifyOrder {
   mockPrinter.printTitle("Create Task State")
   mockPrinter.printTitle("Please, Write your state name")
   mockReader.readInput()
   mockCreateStateUseCase.createState(testStateName)
   mockAddStateUseCase.addStateToProject(projectId = testProjectId, state = mockState)
  }
 }

 @Test
 fun `show should handle empty input`() {
  // Arrange
  val emptyInput = ""
  every { mockReader.readInput() } returns emptyInput
  every { mockCreateStateUseCase.createState(emptyInput) } returns mockState

  // Act
  createProjectStateUi.show()

  // Assert
  verify { mockCreateStateUseCase.createState(emptyInput) }
 }

}

