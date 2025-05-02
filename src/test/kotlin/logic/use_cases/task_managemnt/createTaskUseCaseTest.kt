package logic.use_cases.task_managemnt

import io.mockk.*
import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.exceptions.task_managment_exception.BlankFieldsException
import org.example.logic.exceptions.task_managment_exception.TaskCreationException

import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith


class createTaskUseCaseTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private var createTaskLogUseCase: CreateTaskLogUseCase =mockk(relaxed = true)
    private lateinit var createTaskUseCase: CreateTaskUseCase
    @BeforeEach
    fun setup() {
        createTaskUseCase = CreateTaskUseCase(taskRepository,createTaskLogUseCase)
    }

    @Test
    fun `createTask should create a task and log the action`() {
        // Given
        val name = "Test Task"
        val description = "This is a test task"
        val states = "ToDo"

        // When
        createTaskUseCase.createTask(name, description, states)

        // Then
        verify{ taskRepository.createTask(any()) }
        verify { createTaskLogUseCase.createTaskLog(any(), null, any()) }
    }
    @Test
    fun `createTask should throw exception when title is blank`() {
        val title = "   "
        val description = "desc"
        val state = "ToDo"

        val exception = assertThrows<BlankFieldsException> {
            createTaskUseCase.createTask(title, description, state)
        }

        assertEquals("Title must not be blank", exception.message)
        verify(exactly = 0) { taskRepository.createTask(any()) }
        verify(exactly = 0) { createTaskLogUseCase.createTaskLog(any(), any(), any()) }
    }
    @Test
    fun `createTask should throw exception when create fails`() {
        // Given
        val title = "test"
        val description = "desc"
        val state = "ToDo"
        val exception = TaskCreationException("Failed to create task")
        every { taskRepository.createTask(any()) } returns Result.failure(exception)

        //  Then&When
        assertFailsWith<TaskCreationException> {
            createTaskUseCase.createTask(title,description,state)
        }

        verify(exactly = 0) { createTaskLogUseCase.createTaskLog(any(), null, any()) }
    }
    @Test
    fun `createTask should throw exception when state is blank`() {
        val title = " test"
        val description = " desc"
        val state = "  "

        val exception = assertThrows<BlankFieldsException> {
            createTaskUseCase.createTask(title, description, state)
        }

        assertEquals("State name must not be blank", exception.message)
        verify(exactly = 0) { taskRepository.createTask(any()) }
        verify(exactly = 0) { createTaskLogUseCase.createTaskLog(any(), any(), any()) }
    }

    @Test
    fun `createTask should throw exception when description is blank`() {
        val title = " test"
        val description = " "
        val state = "ToDo"

        val exception = assertThrows<BlankFieldsException> {
            createTaskUseCase.createTask(title, description, state)
        }

        assertEquals("Description must not be blank", exception.message)
        verify(exactly = 0) { taskRepository.createTask(any()) }
        verify(exactly = 0) { createTaskLogUseCase.createTaskLog(any(), any(), any()) }
    }



}