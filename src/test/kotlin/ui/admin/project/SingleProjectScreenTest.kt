package ui.admin.project

import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.example.ui.admin.project.DeleteProjectScreen
import org.example.ui.admin.project.SingleProjectScreen
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SingleProjectScreenTest {
    private val viewer: Viewer = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val deleteProjectScreen: DeleteProjectScreen = mockk(relaxed = true)
    private lateinit var singleProjectScreen: SingleProjectScreen

    @BeforeEach
    fun setUp() {
        singleProjectScreen = SingleProjectScreen(viewer, reader, deleteProjectScreen)
    }

    @Test
    fun `should show the title when showed`() {
        // Given
        singleProjectScreen.project = createProjectHelper(name = "Test Project")
        every { reader.readInt() } returns 1

        // When
        singleProjectScreen.show()

        // Then
        verify(exactly = 1) { viewer.printTitle("Project Test Project") }
    }

    @Test
    fun `should ask the user what they want to do when showed`() {
        // Given
        every { reader.readInt() } returns 1
        singleProjectScreen.project = createProjectHelper(name = "Test Project")

        // When
        singleProjectScreen.show()

        // Then
        verify(exactly = 1) { viewer.printInfoLine("What would you like to do?") }
    }

    @Test
    fun `should display the options to user when showed`() {
        // Given
        every { reader.readInt() } returns 1
        singleProjectScreen.project = createProjectHelper(name = "Test Project")

        // When
        singleProjectScreen.show()

        // Then
        verify(exactly = 1) { viewer.printOptions("Edit project", "Delete project", "View project states") }
    }

    @Test
    fun `should ask for user input after displaying titles and options`() {
        // Given
        singleProjectScreen.project = createProjectHelper(name = "Test Project")
        every { reader.readInt() } returns 1

        // When
        singleProjectScreen.show()

        // Then
        verifyOrder {
            viewer.printTitle("Project Test Project")
            viewer.printInfoLine("What would you like to do?")
            viewer.printOptions("Edit project", "Delete project", "View project states")
            reader.readInt()
        }
    }

    @Test
    fun `should navigate to delete project screen when user choose number 2`() {
        // Given
        singleProjectScreen.project = createProjectHelper(name = "Test Project")
        every { reader.readInt() } returns 2

        // When
        singleProjectScreen.show()

        // Then
        verify(exactly = 1) { deleteProjectScreen.show() }
    }

    @Test
    fun `should print error message and re ask for input if user writes invalid choice`() {
        // Given
        singleProjectScreen.project = createProjectHelper(name = "Test Project")
        every { reader.readInt() } returnsMany listOf(5, 2)

        // When
        singleProjectScreen.show()

        // Then
        verifyOrder {
            viewer.printError("Invalid option")
            deleteProjectScreen.show()
        }
    }
}