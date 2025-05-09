package ui.admin.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import data.csv.model.Project
import org.example.ui.admin.project.SingleProjectScreen
import org.example.ui.common.components.Printer
import org.example.ui.admin.project.ViewProjectsScreen
import org.example.ui.common.components.Reader
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProjectsUiScreenTest {

    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val getAllProjectsUseCases: GetAllProjectsUseCases = mockk()
    private val singleProjectScreen: SingleProjectScreen = mockk(relaxed = true)
    private val getUserByIdUseCase: GetUserByIdUseCase = mockk(relaxed = true)
    private val viewProjectsScreen = ViewProjectsScreen(printer, reader, getAllProjectsUseCases, singleProjectScreen,getUserByIdUseCase)

    @Test
    fun `should display all projects when getAllProjects returns a list of projects`() {
        // Given
        val project1 = Project(
            id = UUID.randomUUID(),
            name = "Project Alpha",
            description = "This is the first project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        val project2 = Project(
            id = UUID.randomUUID(),
            name = "Project Beta",
            description = "This is the second project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(2),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(listOf(project1, project2))

        // When
        viewProjectsScreen.show()

        // Then
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
        // Given
        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(emptyList())

        // When
        viewProjectsScreen.show()

        // Then
        verify { printer.printInfoLine("No projects found.") }
    }

    @Test
    fun `should display an error message when getAllProjects fails`() {
        // Given
        val failureMessage = "Database connection error"
        every { getAllProjectsUseCases.getAllProjects() } returns Result.failure(Exception(failureMessage))

        // When
        viewProjectsScreen.show()

        // Then
        verify { printer.printError("Failed to retrieve projects: $failureMessage") }
    }

    @Test
    fun `should display project options and navigate to the selected project`() {
        // Given
        val project1 = Project(
            id = UUID.randomUUID(),
            name = "Project Alpha",
            description = "This is the first project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        val project2 = Project(
            id = UUID.randomUUID(),
            name = "Project Beta",
            description = "This is the second project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(2),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(listOf(project1, project2))
        every { reader.readInt() } returns 1 // Selecting the first project

        // When
        viewProjectsScreen.show()

        // Then
        verify { printer.printOptions(listOf("Project Alpha", "Project Beta")) }
        verify { singleProjectScreen.project = project1 }
        verify { singleProjectScreen.show() }
    }

    @Test
    fun `should display error and re-prompt when invalid project number is entered`() {
        // Given
        val project1 = Project(
            id = UUID.randomUUID(),
            name = "Project Alpha",
            description = "This is the first project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(listOf(project1))
        every { reader.readInt() } returnsMany listOf(0, 2, 1) // Invalid inputs first, then valid input

        // When
        viewProjectsScreen.show()

        // Then
        verify(exactly = 2) { printer.printError("Invalid project number") } // Invalid input prompts error
        verify { singleProjectScreen.project = project1 } // Finally navigate to valid project
        verify { singleProjectScreen.show() }
    }

    @Test
    fun `should handle null input gracefully`() {
        // Given
        val project1 = Project(
            id = UUID.randomUUID(),
            name = "Project Alpha",
            description = "This is the first project.",
            creatorUserID = UUID.randomUUID(),
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now(),
            state = emptyList()
        )
        every { getAllProjectsUseCases.getAllProjects() } returns Result.success(listOf(project1))
        every { reader.readInt() } returnsMany listOf(null, 1) // Null input first, then valid input

        // When
        viewProjectsScreen.show()

        // Then
        verify { printer.printError("Invalid project number") }
        verify { singleProjectScreen.project = project1 }
        verify { singleProjectScreen.show() }
    }
}