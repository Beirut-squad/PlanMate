package domain.use_case.task

import creator_helper.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.domain.repository.TaskRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.UUID

class GetTaskByStateIdAndProjectIdTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private lateinit var getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        getTaskByStateIdAndProjectId = GetTaskByStateIdAndProjectId(taskRepository)
    }
    @Test
    fun `getTaskByStateIdAndProjectId should return tasks when repository returns tasks`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val task1 = createTaskHelper(projectId = projectId, state = createStateHelper(id = stateId))
        val task2 = createTaskHelper(projectId = projectId, state = createStateHelper(id = stateId))

        coEvery { taskRepository.getTaskByStateIdAndProjectId(projectId, stateId) } returns listOf(task1, task2)

        // When
        val tasks = getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, stateId)

        // Then
        assertEquals(2, tasks.size)
        assertEquals(task1.title, tasks[0].title)
        assertEquals(task2.title, tasks[1].title)

        coVerify(exactly = 1) { taskRepository.getTaskByStateIdAndProjectId(projectId, stateId) }
    }


}