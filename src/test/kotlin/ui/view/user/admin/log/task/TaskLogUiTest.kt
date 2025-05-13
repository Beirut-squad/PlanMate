package ui.view.user.admin.log.task

import creator_helper.*
import domain.use_case.authentication.GetUserByIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.common.Printer
import ui.extensions.formatDateTime

class TaskLogUiTest{

    private lateinit var taskLogUi: TaskLogUi
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var printer: Printer


    @BeforeEach
    fun setUp() {
        getUserByIdUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        taskLogUi = TaskLogUi(getUserByIdUseCase, printer)
    }

    @Test
    fun `should view task log creation if task previous entity not found && current entity is found`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val currentTask = createTaskHelper()
            val taskLog = createTaskLogHelper(currentEntity = currentTask)
            val index = 0
            val userName = user.name
            taskLogUi.displayTaskLog(index, taskLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName created new task ${currentTask?.title} at ${currentTask?.createdAt?.formatDateTime()}"
                )
            }
        }
    }

    @Test
    fun `should view task log deletion if task previous entity is found && current entity is not found`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val previousTask = createTaskHelper()
            val taskLog = createTaskLogHelper(previousEntity = previousTask)
            val index = 0
            val userName = user.name
            taskLogUi.displayTaskLog(index, taskLog)

            // Then
            printer.printCorrectOutput(
                "${index + 1}. User $userName deleted task ${previousTask?.title} at ${previousTask?.updatedAt?.formatDateTime()}"
            )
        }
    }

    @Test
    fun `should print name change if the name has changed`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val currentTask = createTaskHelper( title =  "p1")
            val previousTask = createTaskHelper(title = "p2")
            val taskLog = createTaskLogHelper(previousEntity = previousTask , currentEntity = currentTask)
            val index = 0
            val userName = user.name
            taskLogUi.displayTaskLog(index, taskLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName changed task ${currentTask.title} name from ${previousTask?.title} to ${currentTask?.title} at ${currentTask?.updatedAt?.formatDateTime()}"
                )
            }
        }
    }


    @Test
    fun `should print Description change if the Description has changed`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val currentTask = createTaskHelper(description = "p1")
            val previousTask = createTaskHelper(description = "p2")
            val taskLog = createTaskLogHelper(previousEntity = previousTask , currentEntity = currentTask)
            val index = 0
            val userName = user.name
            taskLogUi.displayTaskLog(index, taskLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName changed task ${currentTask.title} description from ${previousTask?.description} to ${currentTask?.description} at ${currentTask?.updatedAt?.formatDateTime()}"
                )
            }
        }
    }

    @Test
    fun `should print state addition if the state has been changed`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val state1 = createStateHelper(name = "s1")
            val state2 = createStateHelper(name = "s2")


            val currentTask = createTaskHelper(state = state1)
            val previousTask = createTaskHelper(state = state2)

            val taskLog = createTaskLogHelper(previousEntity = previousTask , currentEntity = currentTask)
            val index = 0
            val userName = user.name
            taskLogUi.displayTaskLog(index, taskLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName changed state from ${state2.name} to ${state1.name} in task ${currentTask.title} at ${currentTask.updatedAt.formatDateTime()}"
                )
            }
        }
    }
}