package data.repository

import creator_helper.createTaskHelper
import data.datasource.TaskDataSource
import ui.common.exception.TaskDeletionFailedException
import ui.common.exception.TaskEditFailedException
import ui.common.exception.TaskNotFoundException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TaskRepositoryImplTest {
    private val taskDataSource: TaskDataSource = mockk()
    private lateinit var taskRepository: TaskRepositoryImpl
    private val testTask = createTaskHelper()

    @BeforeEach
    fun setUp() {
        taskRepository = TaskRepositoryImpl(taskDataSource)
    }

    @Test
    fun `createTask should delegate to data source`() = runTest {
        coEvery { taskDataSource.createTask(any()) } returns Unit

        taskRepository.createTask(testTask)

        coVerify { taskDataSource.createTask(testTask) }
    }

    @Test
    fun `editTask should delegate to data source`() = runTest {
        coEvery { taskDataSource.editTask(any()) } returns Unit

        taskRepository.editTask(testTask)

        coVerify { taskDataSource.editTask(testTask) }
    }

    @Test
    fun `editTask should throw TaskEditFailedException when data source throws`() = runTest {
        coEvery { taskDataSource.editTask(any()) } throws TaskEditFailedException()

        assertThrows<TaskEditFailedException> {
            taskRepository.editTask(testTask)
        }
    }

    @Test
    fun `deleteTask should delegate to data source`() = runTest {
        val taskId = UUID.randomUUID()
        coEvery { taskDataSource.deleteTask(any()) } returns Unit

        taskRepository.deleteTask(taskId)

        coVerify { taskDataSource.deleteTask(taskId) }
    }

    @Test
    fun `deleteTask should throw TaskDeletionFailedException when data source throws`() = runTest {
        val taskId = UUID.randomUUID()
        coEvery { taskDataSource.deleteTask(any()) } throws TaskDeletionFailedException()

        assertThrows<TaskDeletionFailedException> {
            taskRepository.deleteTask(taskId)
        }
    }

    @Test
    fun `getAllTasks should return list from data source`() = runTest {
        val tasksList = listOf(testTask)
        coEvery { taskDataSource.getAllTasks() } returns tasksList

        val result = taskRepository.getAllTasks()

        assertEquals(tasksList, result)
        coVerify { taskDataSource.getAllTasks() }
    }

    @Test
    fun `getTask should return task from data source`() = runTest {
        val taskId = UUID.randomUUID()
        coEvery { taskDataSource.getTask(any()) } returns testTask

        val result = taskRepository.getTask(taskId)

        assertEquals(testTask, result)
        coVerify { taskDataSource.getTask(taskId) }
    }

    @Test
    fun `getTask should throw TaskNotFoundException when data source throws`() = runTest {
        val taskId = UUID.randomUUID()
        coEvery { taskDataSource.getTask(any()) } throws TaskNotFoundException()

        assertThrows<TaskNotFoundException> {
            taskRepository.getTask(taskId)
        }
    }

    @Test
    fun `getTasksByStateAndProjectIds should return list from data source`() = runTest {
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val tasksList = listOf(testTask)
        coEvery { taskDataSource.getTasksByStateAndProjectIds(any(), any()) } returns tasksList

        val result = taskRepository.getTaskByStateIdAndProjectId(projectId, stateId)

        assertEquals(tasksList, result)
        coVerify { taskDataSource.getTasksByStateAndProjectIds(projectId, stateId) }
    }

    @Test
    fun `getAllTasksForProject should return list from data source`() = runTest {
        val projectId = UUID.randomUUID()
        val tasksList = listOf(testTask)
        coEvery { taskDataSource.getAllTasksForProject(any()) } returns tasksList

        val result = taskRepository.getAllProjectTasks(projectId)

        assertEquals(tasksList, result)
        coVerify { taskDataSource.getAllTasksForProject(projectId) }
    }
}