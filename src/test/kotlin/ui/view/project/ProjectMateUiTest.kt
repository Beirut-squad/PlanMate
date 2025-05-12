package ui.view.project

import creator_helper.createProjectHelper
import domain.exception.handler.ExceptionHandler
import domain.use_case.project.GetProjectByIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import ui.components.Printer
import ui.components.Reader
import ui.view.task.CreateTaskUi
import ui.view.user.mate.UserProjectsUi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.*

class ProjectMateUiTest {

    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk()
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
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
    fun `show should fetch project and display options`() = runTest {
        val project = createProjectHelper()
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        every { reader.readInt() } returns 0

        projectMateUi.show()

        coVerify { getProjectByIdUseCase.getProjectById(projectId) }
        verify {
            printer.printInfoLine("Project Name: ${project.title}")
            printer.printInfoLine("Choose an option:")
            printer.printOptions(
                "View state for project",
                "View all task for project",
                "Create new task",
                "Enter Any Thing To Go Back"
            )
        }
    }

    @Test
    fun `displayProjectOptions should show correct project details`() = runTest {
        val project = createProjectHelper()
        every { reader.readInt() } returns 0

        projectMateUi.show()

        verify {
            printer.printInfoLine("Project Name: ${project.title}")
            printer.printInfoLine("Description: ${project.description}")
            printer.printInfoLine("Created At: ${project.createdAt}")
        }
    }

    @Test
    fun `should handle option 1 - View state for project`() = runTest {
        val project = createProjectHelper()
        val projectStateUi = mockk<ProjectStateSelectedUi>(relaxed = true)
        every { reader.readInt() } returns 1 andThen 0
        mockkConstructor(ProjectStateSelectedUi::class)
        coEvery { anyConstructed<ProjectStateSelectedUi>().show() } just Runs

        projectMateUi.show()

        coVerify { projectMateUi.show() }
    }

    @Test
    fun `should handle option 2 - View all tasks for project`() = runTest {
        val project = createProjectHelper()
        val projectTasksUi = mockk<ProjectTasksUi>(relaxed = true)
        every { reader.readInt() } returns 2 andThen 0
        mockkConstructor(ProjectTasksUi::class)
        coEvery { anyConstructed<ProjectTasksUi>().show() } just Runs

        projectMateUi.show()

        verify { ProjectTasksUi(projectId) }
    }

    @Test
    fun `should handle option 3 - Create new task when project has states`() = runTest {
        val project = createProjectHelper()
        val createTaskUi = mockk<CreateTaskUi>(relaxed = true)
        every { reader.readInt() } returns 3 andThen 0
        mockkConstructor(CreateTaskUi::class)
        coEvery { anyConstructed<CreateTaskUi>().show() } just Runs

        projectMateUi.show()

        verify { CreateTaskUi(projectId) }
    }

    @Test
    fun `should show error when creating task with no states`() = runTest {
        val project = createProjectHelper(state = emptyList())
        val userProjectsUi = mockk<UserProjectsUi>(relaxed = true)
        every { reader.readInt() } returns 3 andThen 0
        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        projectMateUi.show()

        verify {
            printer.printError("Cannot create a task because this project has no states.")
            UserProjectsUi()
        }
    }

    @Test
    fun `should exit on invalid option`() = runTest {
        val project = createProjectHelper()
        every { reader.readInt() } returns 999

        projectMateUi.show()

        verify(exactly = 0) {
            ProjectStateSelectedUi(projectId)
            ProjectTasksUi(projectId)
            CreateTaskUi(projectId)
        }
    }

//    @Test
//    fun `should handle exception when fetching project`() = runTest {
//        val exception = RuntimeException("Failed to fetch project")
//        coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws exception
//        coEvery { exceptionHandler.tryCatchingAsyncWithResult(any(), any(), any()) } answers {
//            firstArg<suspend () -> Any>().invoke()
//        }
//
//        projectMateUi.show()
//
//        coVerify { exceptionHandler.tryCatchingAsyncWithResult(any(), any(), any()) }
//    }
}