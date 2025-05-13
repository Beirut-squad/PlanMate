package domain.use_case.task

import creator_helper.createTaskHelper
import domain.repository.TaskRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.UUID

class GetProjectTasksUseCaseTest {
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private lateinit var getProjectTasksUseCase: GetProjectTasksUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        getProjectTasksUseCase = GetProjectTasksUseCase(taskRepository)
    }

    @Test
    fun `getProjectTasks should call repository with correct project ID`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val taskList = listOf(createTaskHelper(projectId = projectId))
        coEvery { taskRepository.getAllTasksForProject(any()) } returns taskList

        // When
        getProjectTasksUseCase.getProjectTasks(projectId)

        // Then
        coVerify(exactly = 1) { taskRepository.getAllTasksForProject(projectId) }
    }

    @Test
    fun `getProjectTasks should return list of tasks from repository`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val task1 = createTaskHelper(projectId = projectId)
        val task2 = createTaskHelper(projectId = projectId)
        val task3 = createTaskHelper(projectId = projectId)
        val expectedTasks = listOf(task1, task2, task3)

        coEvery { taskRepository.getAllTasksForProject(projectId) } returns expectedTasks

        // When
        val result = getProjectTasksUseCase.getProjectTasks(projectId)

        // Then
        assertEquals(3, result.size)
        assertEquals(expectedTasks, result)
    }

    @Test
    fun `getProjectTasks should return specific tasks for the requested project ID`() = runTest {
        // Given
        val projectId1 = UUID.randomUUID()
        val projectId2 = UUID.randomUUID()

        val tasksForProject1 = listOf(
            createTaskHelper(projectId = projectId1),
            createTaskHelper(projectId = projectId1)
        )

        val tasksForProject2 = listOf(
            createTaskHelper(projectId = projectId2)
        )

        coEvery { taskRepository.getAllTasksForProject(projectId1) } returns tasksForProject1
        coEvery { taskRepository.getAllTasksForProject(projectId2) } returns tasksForProject2

        // When
        val result1 = getProjectTasksUseCase.getProjectTasks(projectId1)
        val result2 = getProjectTasksUseCase.getProjectTasks(projectId2)

        // Then
        assertEquals(2, result1.size)
        assertEquals(tasksForProject1, result1)
        assertEquals(1, result2.size)
        assertEquals(tasksForProject2, result2)
    }

    @Test
    fun `getProjectTasks should ensure task list contains only tasks for requested project`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val otherProjectId = UUID.randomUUID()

        val tasks = listOf(
            createTaskHelper(projectId = projectId),
            createTaskHelper(projectId = projectId),
            createTaskHelper(projectId = projectId)
        )

        coEvery { taskRepository.getAllTasksForProject(projectId) } returns tasks

        // When
        val result = getProjectTasksUseCase.getProjectTasks(projectId)

        // Then
        assertEquals(3, result.size)
        assertTrue(result.all { it.projectId == projectId })
        assertTrue(result.none { it.projectId == otherProjectId })
    }
}