package ui.common.screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.data.model.Project
import org.example.ui.common.components.Printer
import org.example.ui.common.screens.ViewProjectsUI
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProjectsUiScreenTest {

    private val printer: Printer = mockk(relaxed = true) // Mocked viewer, relaxed for convenient stubbing
    private val getAllProjectsUseCases: GetAllProjectsUseCases = mockk() // Mocked use case
    private val viewProjectsScreen = ViewProjectsUI(printer, getAllProjectsUseCases) // System under test

    @Test
    fun `should display all projects when getAllProjects returns a list of projects`() {
        // Arrange: Setup mocked use case to return a successful list of projects
        val project1 = Project(
            id = UUID.randomUUID(),
            title = "Project Alpha",
            description = "This is the first project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        val project2 = Project(
            id = UUID.randomUUID(),
            title = "Project Beta",
            description = "This is the second project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(2),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(listOf(project1, project2))

        // Act: Execute the show method
        viewProjectsScreen.show()

        // Assert: Verify that the viewer printed the correct project details
        verify { printer.printTitle("Project: ") }
        verify {
            printer.printInfoLine(
                match {
                    it.contains("1.") &&
                            it.contains("Project Alpha") &&
                            it.contains("This is the first project.") &&
                            it.contains(project1.creatorUserID.toString())
                }
            )
        }
        verify {
            printer.printInfoLine(
                match {
                    it.contains("2.") &&
                            it.contains("Project Beta") &&
                            it.contains("This is the second project.") &&
                            it.contains(project2.creatorUserID.toString())
                }
            )
        }
    }

    @Test
    fun `should display no projects found when getAllProjects returns an empty list`() {
        // Arrange: Setup mocked use case to return an empty list
        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(emptyList())

        // Act: Execute the show method
        viewProjectsScreen.show()

        // Assert: Verify that the viewer displayed "No projects found."
        verify { printer.printInfoLine("No projects found.") }
    }

    @Test
    fun `should display an error message when getAllProjects fails`() {
        // Arrange: Setup mocked use case to return a failure
        val failureMessage = "Database connection error"
        every { getAllProjectsUseCases.getAllProjects() } returns Result.failure(Exception(failureMessage))

        // Act: Execute the show method
        viewProjectsScreen.show()

        // Assert: Verify that the viewer displayed the error message
        verify { printer.printError("Failed to retrieve projects: $failureMessage") }
    }
}