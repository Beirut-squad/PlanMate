package ui.view.project

import creator_helper.createProjectHelper
import domain.exception.ProjectNotFoundException
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.use_case.project.GetProjectByIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import ui.components.Printer
import ui.components.Reader
import java.util.*
import kotlin.test.Test

class ProjectMateUiTest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk(relaxed = true)
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private val projectId = UUID.randomUUID()
    private lateinit var projectMateUi: ProjectMateUi

    @BeforeEach
    fun setup() {
        startKoin {
            modules(
                module {
                    single { printer }
                    single { reader }
                    single { getProjectByIdUseCase }
                    single { exceptionHandler }
                    single { executor }
                }
            )
        }

        projectMateUi = ProjectMateUi(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        clearAllMocks()
    }

    @Test
    fun `show project details when project exists`() = runTest {
        // Arrange
        val mockProject = createProjectHelper()

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns 4 // Option to go back

        // Act
        projectMateUi.show()

        // Assert
        coVerify {
            printer.printInfoLine(
                """
                - Project Name : ${mockProject.title}
                - Description : ${mockProject.description}
                - Created At : ${mockProject.createdAt}
            """.trimIndent(),
                true
            )
            printer.printInfoLine("Choose an option :", true)
            printer.printOptions(
                "View state for project",
                "View all task for project",
                "Create new task",
                "Enter Any Thing To Go Back"
            )
        }
    }

    @Test
    fun `handle error when fetching project fails`() = runTest {
        // Arrange
        val exception =  ProjectNotFoundException()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws exception

        // Act
        projectMateUi.show()

        // Assert
        coVerify {
            exceptionHandler.printHandledError(exception)
        }
    }

    @Test
    fun `cannot create task when project has no states`() = runTest {
        // Arrange
        val mockProject = createProjectHelper(state = emptyList())
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns 3 andThen 4 // First try to create task, then exit

        // Act
        projectMateUi.show()

        // Assert
        verify {
            printer.printError("Can't create a task because this project has no states.")
        }
    }

    @Test
    fun `navigate to project states when option 1 is selected`() = runTest {
        // Arrange
        val mockProject = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns 1 andThen 4 // First select view states, then exit

        // Act
        projectMateUi.show()
    }

    @Test
    fun `navigate to project tasks when option 2 is selected`() = runTest {
        // Arrange
        val mockProject = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns 2 andThen 4 // First select view tasks, then exit

        // Act
        projectMateUi.show()
    }

    @Test
    fun `navigate to create task when option 3 is selected and project has states`() = runTest {
        // Arrange
        val mockProject = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns 3 andThen 4 // First select create task, then exit

        // Act
        projectMateUi.show()

    }
}