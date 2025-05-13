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
import ui.view.task.CreateTaskUi
import ui.view.user.mate.UserProjectsUi
import java.util.*
import kotlin.test.Test

class ProjectMateUiTest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk(relaxed = true)
    private val userProjectsUi: UserProjectsUi = mockk(relaxed = true)
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
    fun `should display project details when project exists`() = runTest {
        // Given
        val project = createProjectHelper()

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        every { reader.readInt() } returns 4 // Option to go back

        // When
        projectMateUi.show()

        // Then
        verify {
            printer.printInfoLine(
                """
                - Project Name : ${project.title}
                - Description : ${project.description}
                - Created At : ${project.createdAt}
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
    fun `should handle error when fetching project fails`() = runTest {
        // Given
        val exception =  ProjectNotFoundException()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws exception

        // When
        projectMateUi.show()

        // Then
        coVerify {
            exceptionHandler.printHandledError(exception)
        }
    }

    @Test
    fun `should cannot create task when project has no states`() = runTest {
        // Given
        val project = createProjectHelper(state = emptyList())
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        every { reader.readInt() } returns 3 andThen 4 // First try to create task, then exit

        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        // When
        projectMateUi.show()

        // Then
        verify {
            printer.printError("Can't create a task because this project has no states.")
        }
        coVerify { anyConstructed<UserProjectsUi>().show() }
    }

    @Test
    fun `should navigate to project states when option 1 is selected`() = runTest {
        // Given
        val project = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        every { reader.readInt() } returns 1 andThen 4 // First select view states, then exit

        mockkConstructor(ProjectStateSelectedUi::class)
        coEvery { anyConstructed<ProjectStateSelectedUi>().show() } just Runs
        // When
        projectMateUi.show()

        //Then
        coVerify { anyConstructed<ProjectStateSelectedUi>().show() }
    }

    @Test
    fun `should navigate to project tasks when option 2 is selected`() = runTest {
        // Given
        val mockProject = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns 2 andThen 4 // First select view tasks, then exit

        mockkConstructor(ProjectTasksUi::class)
        coEvery { anyConstructed<ProjectTasksUi>().show() } just Runs
        // When
        projectMateUi.show()

        //Then
        coVerify { anyConstructed<ProjectTasksUi>().show() }
    }

    @Test
    fun `should navigate to create task when option 3 is selected and project has states`() = runTest {
        // Given
        val mockProject = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns 3 andThen 4 // First select create task, then exit

        mockkConstructor(CreateTaskUi::class)
        coEvery { anyConstructed<CreateTaskUi>().show() } just Runs
        // When
        projectMateUi.show()

        //Then
        coVerify { anyConstructed<CreateTaskUi>().show() }

    }

    @Test
    fun `should navigate to previous screen when option null `() = runTest {
        // Given
        val mockProject = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockProject
        every { reader.readInt() } returns null

        // When
        projectMateUi.show()

        //Then
        coVerify(exactly = 0) { projectMateUi.show() }
    }

}