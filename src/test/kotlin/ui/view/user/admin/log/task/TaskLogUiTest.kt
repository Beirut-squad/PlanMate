package ui.view.user.admin.log.task

import creator_helper.*
import domain.use_case.authentication.GetUserByIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.components.Printer
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
}