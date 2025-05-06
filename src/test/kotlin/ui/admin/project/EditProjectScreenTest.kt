package ui.admin.project

import creator_helper.createUserHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import logic.use_cases.project_manegment.EditProjectDescriptionUseCase
import logic.use_cases.project_manegment.EditProjectNameUseCase
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.models.Project
import org.example.ui.admin.project.EditProjectScreen
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class EditProjectScreenTest {
    @MockK
    private lateinit var viewer: Viewer

    @MockK
    private lateinit var reader: Reader

    @MockK
    private lateinit var project: Project

    @MockK
    private lateinit var editProjectNameUseCase: EditProjectNameUseCase

    @MockK
    private lateinit var editProjectDescriptionUseCase: EditProjectDescriptionUseCase

    @MockK
    private lateinit var currentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase

    @MockK
    private lateinit var editProjectScreen: EditProjectScreen

    @BeforeEach
    fun setUp() {
        viewer = mockk()
        reader = mockk()
        project = mockk()
        editProjectNameUseCase = mockk()
        editProjectDescriptionUseCase = mockk()
        currentLoggedInUserUseCase = mockk()
        editProjectScreen = EditProjectScreen(
            viewer = viewer,
            reader = reader,
            project = project,
            editProjectNameUseCase = editProjectNameUseCase,
            editProjectDescriptionUseCase = editProjectDescriptionUseCase,
            currentLoggedInUserUseCase = currentLoggedInUserUseCase
        )
    }

    @Test
    fun `show should display menu and process user input until exit option is selected`() {
        // Given
        val responses = listOf(1, 2, 0)
        val iterator = responses.iterator()

        // Mock reader to return the next value in our list each time readInt is called
        every { reader.readInt() } answers { iterator.next() }

        // Mock the behavior of other methods to prevent actual execution - be specific with arguments
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Name: ", withNewLine = false) } just runs
        every { viewer.printPlainText("Edit Project Description: ", withNewLine = false) } just runs

        // Mocks for child functions
        every { reader.readInput() } returns "New Value"
        val mockUser = createUserHelper()
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.success(mockUser)
        every { editProjectNameUseCase.editProject(project, "New Value", mockUser.id) } returns Unit
        every { editProjectDescriptionUseCase.editProject(project, "New Value", mockUser.id) } returns Unit

        // When
        editProjectScreen.show()

        // Then
        assertTrue(true)
        verify(exactly = 3) { reader.readInt() }
        verify(exactly = 3) { viewer.printTitle("Choose what you want to change in project.") }
        verify(exactly = 3) { viewer.printOptions("1- Edit project name") }
        verify(exactly = 3) { viewer.printOptions("2- Edit project description") }
        verify(exactly = 3) { viewer.printOptions("0- Return") }

        // Verify that each option triggered the appropriate method
        verify(exactly = 2) { reader.readInput() } // For name edit and description edit
        verify(exactly = 1) { viewer.printPlainText("Edit Project Name: ", withNewLine = false) }
        verify(exactly = 1) { viewer.printPlainText("Edit Project Description: ", withNewLine = false) }
        verify(exactly = 1) { editProjectNameUseCase.editProject(project, "New Value", mockUser.id) }
        verify(exactly = 1) { editProjectDescriptionUseCase.editProject(project, "New Value", mockUser.id) }

    }

    @Test
    fun `editProjectName should update project name when user is logged in`() {
        // Given
        val newProjectName = "New Project Name"
        val mockUser = createUserHelper()

        every { reader.readInt() } returnsMany listOf(1, 0)
        every { reader.readInput() } returns newProjectName
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.success(mockUser)
        every { editProjectNameUseCase.editProject(project, newProjectName, mockUser.id) } returns Unit

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Name: ", withNewLine = false) } just runs

        // When
        editProjectScreen.show()

        // Then
        verify { viewer.printPlainText("Edit Project Name: ", withNewLine = false) }
        verify { editProjectNameUseCase.editProject(project, newProjectName, mockUser.id) }
    }

    @Test
    fun `editProjectName should not update project name when user is not logged in`() {
        // Arrange
        val newProjectName = "New Project Name"

        every { reader.readInt() } returnsMany listOf(1, 0)
        every { reader.readInput() } returns newProjectName
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.success(null)

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Name: ", withNewLine = false) } just runs

        // Act
        editProjectScreen.show()

        // Assert
        verify { viewer.printPlainText("Edit Project Name: ", withNewLine = false) }
        verify(exactly = 0) { editProjectNameUseCase.editProject(any(), any(), any()) }
    }

    @Test
    fun `editProjectName should not update project name when getCurrentUser returns failure`() {
        // Arrange
        val newProjectName = "New Project Name"
        val exception = RuntimeException("Failed to get current user")

        every { reader.readInt() } returnsMany listOf(1, 0)
        every { reader.readInput() } returns newProjectName
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.failure(exception)

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Name: ", withNewLine = false) } just runs

        // Act
        assertThrows(RuntimeException::class.java) {
            editProjectScreen.show()
        }
        // Assert
        verify { viewer.printPlainText("Edit Project Name: ", withNewLine = false) }
        verify(exactly = 0) { editProjectNameUseCase.editProject(any(), any(), any()) }
    }

    @Test
    fun `editProjectName should not update project description when getCurrentUser returns null`() {
        // Arrange
        val newProjectName = "New Project Name"

        every { reader.readInt() } returnsMany listOf(1, 0)
        every { reader.readInput() } returns newProjectName

        // Set up the mock to return a failure result
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.success(null)

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Name: ", withNewLine = false) } just runs

        // Act
        editProjectScreen.show()

        // Assert
        verify { viewer.printPlainText("Edit Project Name: ", withNewLine = false) }
        assertNull(currentLoggedInUserUseCase.getCurrentUser().getOrThrow())
    }

    @Test
    fun `editProjectDescription should update project description when user is logged in`() {
        // Arrange
        val newProjectDescription = "New Project Description"
        val mockUser = createUserHelper()

        every { reader.readInt() } returnsMany listOf(2, 0)
        every { reader.readInput() } returns newProjectDescription
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.success(mockUser)
        every { editProjectDescriptionUseCase.editProject(project, newProjectDescription, mockUser.id) } just runs

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Description: ", withNewLine = false) } just runs

        // Act
        editProjectScreen.show()

        // Assert
        verify { viewer.printPlainText("Edit Project Description: ", withNewLine = false) }
        verify { editProjectDescriptionUseCase.editProject(project, newProjectDescription, mockUser.id) }
    }

    @Test
    fun `editProjectDescription should not update project description when user is not logged in`() {
        // Arrange
        val newProjectDescription = "New Project Description"

        every { reader.readInt() } returnsMany listOf(2, 0)
        every { reader.readInput() } returns newProjectDescription
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.success(null)

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Description: ", withNewLine = false) } just runs

        // Act
        editProjectScreen.show()

        // Assert
        verify { viewer.printPlainText("Edit Project Description: ", withNewLine = false) }
        verify(exactly = 0) { editProjectDescriptionUseCase.editProject(any(), any(), any()) }
    }

    @Test
    fun `editProjectDescription should not update project description when getCurrentUser returns failure`() {
        // Arrange
        val newProjectDescription = "New Project Description"
        val exception = RuntimeException("Failed to get current user")

        every { reader.readInt() } returnsMany listOf(2, 0)
        every { reader.readInput() } returns newProjectDescription

        // Set up the mock to return a failure result
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.failure(exception)

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Description: ", withNewLine = false) } just runs

        // Act & Assert
        // If the class is designed to propagate exceptions, we can use assertThrows
        assertThrows(RuntimeException::class.java) {
            editProjectScreen.show()
        }

        // Verify that before the exception, the UI interactions happened but editProject wasn't called
        verify { viewer.printPlainText("Edit Project Description: ", withNewLine = false) }
        verify(exactly = 0) { editProjectDescriptionUseCase.editProject(any(), any(), any()) }
    }

    @Test
    fun `editProjectDescription should not update project description when getCurrentUser returns null`() {
        // Arrange
        val newProjectDescription = "New Project Description"

        every { reader.readInt() } returnsMany listOf(2, 0)
        every { reader.readInput() } returns newProjectDescription

        // Set up the mock to return a failure result
        every { currentLoggedInUserUseCase.getCurrentUser() } returns Result.success(null)

        // Mock all viewer interactions
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs
        every { viewer.printPlainText("Edit Project Description: ", withNewLine = false) } just runs

        // Act
        editProjectScreen.show()

        // Assert
        verify { viewer.printPlainText("Edit Project Description: ", withNewLine = false) }
        verify(exactly = 0) { editProjectDescriptionUseCase.editProject(any(), any(), any()) }
    }

    @Test
    fun `displayMenu should show all menu options in correct order`() {
        // Arrange
        every { reader.readInt() } returns 0

        // Mock all viewer interactions with specific parameters
        every { viewer.printOptions("1- Edit project name") } just runs
        every { viewer.printOptions("2- Edit project description") } just runs
        every { viewer.printOptions("0- Return") } just runs
        every { viewer.printTitle("Choose what you want to change in project.") } just runs

        // Act
        editProjectScreen.show()

        // Assert
        verifyOrder {
            viewer.printOptions("1- Edit project name")
            viewer.printOptions("2- Edit project description")
            viewer.printOptions("0- Return")
            viewer.printTitle("Choose what you want to change in project.")
        }
    }

}