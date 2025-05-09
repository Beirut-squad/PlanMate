package org.example.ui.common.screens

import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import io.mockk.*
import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Printer
import org.example.ui.mate.home_screen.ViewProjectsForUserUI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.UUID
import kotlin.test.Test

class ViewAllTaskDataSourceWriterParserColumnIndexForProjectFakeFakeDataSourceWriterParserColumnIndexUITest {
    private var printer: Printer = mockk(relaxed = true)
    private var reader: Reader = mockk(relaxed = true)
    private var getTasksForProjectUseCase: GetTasksForProjectUseCase = mockk(relaxed = true)

    private val projectId = UUID.randomUUID()
    private val taskId = UUID.randomUUID()
    private val state = createStateHelper(UUID.randomUUID(), "To Do")
    private val task = createTaskHelper(
        id = taskId,
        title = "Test Task",
        description = "Test Description",
        state = state,
        projectId = projectId
    )

    private lateinit var viewAllTaskForProjectUI: ViewAllTaskForProjectUI

    @BeforeEach
    fun setup() {
        stopKoin()

        mockkConstructor(ViewProjectsForUserUI::class)
        every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

        mockkConstructor(EditTaskUI::class)
        every { anyConstructed<EditTaskUI>().show() } just Runs

        mockkConstructor(DeleteTaskUI::class)
        every { anyConstructed<DeleteTaskUI>().show() } just Runs

        startKoin {
            modules(module {
                single { printer }
                single { reader }
                single { getTasksForProjectUseCase }
            })
        }

        viewAllTaskForProjectUI = ViewAllTaskForProjectUI(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        unmockkAll()
    }

    @Test
    fun `should show message when no tasks are available`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(emptyList())

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printInfoLine("No tasks found for this project.") }
        verify { anyConstructed<ViewProjectsForUserUI>().show() }
    }

    @Test
    fun `should show error when task retrieval fails`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns
                Result.failure(Exception("Failed to get tasks"))

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printError("An error occurred while retrieving tasks: Failed to get tasks") }
    }

    @Test
    fun `should display tasks and menu options when tasks exist`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printTitle("Tasks for Project:") }
        verify { printer.printInfoLine("To Do".padEnd(30)) }
        verify { printer.printInfoLine("-".repeat(30)) }
        verify { printer.printInfoLine("Test Task".padEnd(30)) }
        verify { printer.printGoodbyeMessage("Goodbye") }
        verify { anyConstructed<ViewProjectsForUserUI>().show() }
    }

    @Test
    fun `should display multiple tasks with the same state correctly`() {
        // Given
        val task2 = createTaskHelper(
            id = UUID.randomUUID(),
            title = "Another Task",
            description = "Another Description",
            state = state,
            projectId = projectId
        )
        val tasks = listOf(task, task2)

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printInfoLine("To Do".padEnd(30)) }
        verify { printer.printInfoLine("-".repeat(30)) }
        verify { printer.printInfoLine("Test Task".padEnd(30)) }
        verify { printer.printInfoLine("Another Task".padEnd(30)) }
    }

    @Test
    fun `should display tasks with multiple states correctly`() {
        // Given
        val inProgressState = createStateHelper(UUID.randomUUID(), "In Progress")
        val inProgressTask = createTaskHelper(
            id = UUID.randomUUID(),
            title = "In Progress Task",
            description = "In Progress Description",
            state = inProgressState,
            projectId = projectId
        )
        val tasks = listOf(task, inProgressTask)

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printInfoLine(match { it.contains("To Do") && it.contains("In Progress") }) }
        verify { printer.printInfoLine("-".repeat(60)) }
        verify { printer.printInfoLine(match { it.contains("Test Task") && it.contains("In Progress Task") }) }
    }

    @Test
    fun `should handle different number of tasks in different states`() {
        // Given
        val inProgressState = createStateHelper(UUID.randomUUID(), "In Progress")
        val doneState = createStateHelper(UUID.randomUUID(), "Done")

        val inProgressTask1 = createTaskHelper(
            id = UUID.randomUUID(),
            title = "In Progress Task 1",
            description = "Description 1",
            state = inProgressState,
            projectId = projectId
        )

        val inProgressTask2 = createTaskHelper(
            id = UUID.randomUUID(),
            title = "In Progress Task 2",
            description = "Description 2",
            state = inProgressState,
            projectId = projectId
        )

        val doneTask = createTaskHelper(
            id = UUID.randomUUID(),
            title = "Done Task",
            description = "Done Description",
            state = doneState,
            projectId = projectId
        )

        val tasks = listOf(task, inProgressTask1, inProgressTask2, doneTask)

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printInfoLine(match {
            it.contains("To Do") && it.contains("In Progress") && it.contains("Done")
        })}

        verify { printer.printInfoLine("-".repeat(90)) }

        verify { printer.printInfoLine(match {
            it.contains("Test Task") && it.contains("In Progress Task 1") && it.contains("Done Task")
        })}

        verify { printer.printInfoLine(match { it.contains("In Progress Task 2") }) }
    }

    @Test
    fun `should handle state with no tasks`() {
        // Given
        val doneState = createStateHelper(UUID.randomUUID(), "Done")
        val doneTask = createTaskHelper(
            id = UUID.randomUUID(),
            title = "Done Task",
            description = "Done Description",
            state = doneState,
            projectId = projectId
        )

        val tasks = listOf(task, doneTask)

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printInfoLine(match {
            it.contains("To Do") && it.contains("Done") && !it.contains("In Progress")
        })}

        verify { printer.printInfoLine("-".repeat(60)) }

        verify { printer.printInfoLine(match {
            it.contains("Test Task") && it.contains("Done Task")
        })}
    }

    @Test
    fun `should navigate to edit task screen when option 1 is selected`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "1"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { anyConstructed<EditTaskUI>().show() }
    }

    @Test
    fun `should navigate to delete task screen when option 2 is selected`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "2"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { anyConstructed<DeleteTaskUI>().show() }
    }

    @Test
    fun `should exit when option 3 is selected`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printGoodbyeMessage("Goodbye") }
        verify { anyConstructed<ViewProjectsForUserUI>().show() }
    }

    @Test
    fun `should handle non-numeric input for menu selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "abc"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printGoodbyeMessage("Goodbye") }
        verify { anyConstructed<ViewProjectsForUserUI>().show() }
    }

    @Test
    fun `should handle null input for menu selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns null

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printGoodbyeMessage("Goodbye") }
        verify { anyConstructed<ViewProjectsForUserUI>().show() }
    }

    @Test
    fun `should verify correct constructor arguments when navigating to EditTaskUI`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "1"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { EditTaskUI(projectId).show() }
    }

    @Test
    fun `should verify correct constructor arguments when navigating to DeleteTaskUI`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "2"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { DeleteTaskUI(projectId).show() }
    }

    @Test
    fun `should verify correct constructor arguments when navigating to ViewProjectsForUserUI`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { ViewProjectsForUserUI().show() }
    }

    @Test
    fun `should handle task navigation correctly when navigating through menu options`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "1"  // Edit task option

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { anyConstructed<EditTaskUI>().show() }

        // Given a new input for delete task option
        every { reader.readInput() } returns "2"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { anyConstructed<DeleteTaskUI>().show() }
    }

    @Test
    fun `should handle project view navigation correctly when selecting exit option`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { printer.printGoodbyeMessage("Goodbye") }
        verify { anyConstructed<ViewProjectsForUserUI>().show() }
    }


}
