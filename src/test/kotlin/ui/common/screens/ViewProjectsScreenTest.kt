package ui.common.screens

import io.mockk.*
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.models.Project
import org.example.ui.admin.project.CreateProjectStateUi
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectsScreen
import org.junit.jupiter.api.MethodOrdererContext
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ViewProjectsScreenTest {

    private val viewer: Viewer = mockk(relaxed = true) // Mocked viewer, relaxed for convenient stubbing
    private val getAllProjectsUseCases: GetAllProjectsUseCases = mockk() // Mocked use case
    private val createProjectStateUi: CreateProjectStateUi = mockk()
    private val viewProjectsScreen = ViewProjectsScreen(viewer, getAllProjectsUseCases) // System under test

    @Test
    fun `should redirect to state creation when a project has no state`() {
        // Arrange
        val project = Project(
            id = UUID.randomUUID(),
            name = "Project Without State",
            description = "No state yet",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(3),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )

        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(listOf(project))

        mockkConstructor(CreateProjectStateUi::class)
        every { anyConstructed<CreateProjectStateUi>().show() } just Runs

        // Act
        viewProjectsScreen.show()

        // Assert
        verify {
            viewer.printError("Project '${project.name}' has no state. Redirecting to state creation...")
            anyConstructed<CreateProjectStateUi>().show()
        }
    }

    @Test
    fun `should display an error message when getAllProjects fails`() {
        // Arrange: Setup mocked use case to return a failure
        val failureMessage = "Database connection error"
        every { getAllProjectsUseCases.getAllProjects() } returns Result.failure(Exception(failureMessage))

        // Act: Execute the show method
        viewProjectsScreen.show()

        // Assert: Verify that the viewer displayed the error message
        verify { viewer.printError("Failed to retrieve projects: $failureMessage") }
    }
}