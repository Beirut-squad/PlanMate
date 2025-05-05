package ui.common.screens

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.CreateNewTaskUI
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertNotNull

class CreateNewTaskUITest {
    private var viewer: Viewer = mockk(relaxed =true )
    private var reader: Reader = mockk(relaxed =true )
    private var getCurrentUserUseCase: GetCurrentLoggedInUserUseCase= mockk(relaxed =true )
    private var createTaskUseCase: CreateTaskUseCase= mockk(relaxed =true )
    private val getProjectByIdUseCase :GetProjectByIdUseCase =mockk(relaxed =true )

    private val userId =UUID.randomUUID()
    private val ProjectId =UUID.randomUUID()
    private val user = createUserHelper("mostafa", "testUser", "test@example.com", userId)
    val  state = createStateHelper(UUID.randomUUID(), "To Do")
    val project = createProjectHelper(id = ProjectId, state = listOf(state))
    private lateinit var createNewTaskUI :CreateNewTaskUI

    @BeforeEach
    fun setup() {

        createNewTaskUI = CreateNewTaskUI(viewer,reader,getCurrentUserUseCase,ProjectId,createTaskUseCase,getProjectByIdUseCase)
    }

    @Test
    fun `should create task when all inputs are valid`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("My Task", "My Description", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            createTaskUseCase.createTask(
                "My Task",
                "My Description",
                state,
                ProjectId,
                userId
            )
        }
    }

    @Test
    fun `should show error when project is not found`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.failure(Exception("Project not found"))

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Failed to retrieve project: Project not found") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }
    @Test
    fun `should show error when current user is not available`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.failure(Exception("No user"))

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("No user found") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }
    @Test
    fun `should show error when invalid state index is entered`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Description", "10")

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Invalid state selection.") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }
    @Test
    fun `should cancel task creation when input is empty`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf(null, null, "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            createTaskUseCase.createTask(
                "",
                "",
                state,
                ProjectId,
                userId
            )
        }
    }
    @Test
    fun `should show error when state input is not a number`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "abc")

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Invalid state selection.") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }
    @Test
    fun `should show error when state index is out of bounds`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "10")

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Invalid state selection.") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should show error when state index is zero even though input is not null`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "0") // State index 0 (invalid)

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Invalid state selection.") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }
    @Test
    fun `should select valid state when index is within valid range`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "1") // Valid index

        // When
        createNewTaskUI.show()

        // Then
        verify {
            createTaskUseCase.createTask(
                "Task",
                "Desc",
                state,
                ProjectId,
                userId
            )
        }
    }
    @Test
    fun `should show error when state index is out of bounds even though input is not null`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "5") //

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Invalid state selection.") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should show error when state index is zero (0)`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "0")

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Invalid state selection.") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }



}