package ui.view.user.admin.log.task

import creator_helper.createTaskLogHelper
import ui.common.exception.handler.ExceptionHandler
import ui.common.exception.handler.SafeExecutor
import domain.useCase.authentication.GetUserByIdUseCase
import domain.useCase.log.GetAllTaskLogsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.common.Printer

class TaskLogsUiTest{

    private lateinit var taskLogsUi: TaskLogsUi
    private lateinit var getAllTaskLogsUseCase: GetAllTaskLogsUseCase
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var printer: Printer
    private lateinit var executor: SafeExecutor
    private lateinit var handler: ExceptionHandler

    @BeforeEach
    fun setUp() {
        getAllTaskLogsUseCase = mockk(relaxed = true)
        getUserByIdUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        executor = mockk(relaxed = true)
        handler = mockk(relaxed = true)

        taskLogsUi = TaskLogsUi(
            getAllTaskLogsUseCase,
            getUserByIdUseCase,
            printer,
            executor,
            handler
        )
    }

    @Test
    fun `should call executor to try to execute`() = runTest {
        // Given
        val taskLog = createTaskLogHelper()
        val taskLogs = listOf(taskLog, taskLog)
        coEvery { getAllTaskLogsUseCase.getAllTaskLogs() } returns taskLogs

        val actionSlot = slot<suspend () -> List<Any>>()
        val successSlot = slot<suspend (List<Any>) -> Unit>()
        val errorSlot = slot<suspend (Throwable) -> Unit>()

        // When
        taskLogsUi.show()

        // Then
        coVerify {
            executor.tryToExecute(
                action = capture(actionSlot),
                onSuccess = capture(successSlot),
                onError = capture(errorSlot)
            )
        }
    }
}