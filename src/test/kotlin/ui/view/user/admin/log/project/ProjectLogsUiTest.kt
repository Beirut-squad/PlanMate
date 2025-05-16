package ui.view.user.admin.log.project

import creator_helper.createProjectLogHelper
import ui.common.exception.handler.ExceptionHandler
import ui.common.exception.handler.SafeExecutor
import domain.useCase.authentication.GetUserByIdUseCase
import domain.useCase.log.GetAllProjectLogsUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.common.Printer

class ProjectLogsUiTest{

    private lateinit var projectLogsUi: ProjectLogsUi
    private lateinit var getAllProjectLogsUseCase: GetAllProjectLogsUseCase
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var printer: Printer
    private lateinit var executor: SafeExecutor
    private lateinit var handler: ExceptionHandler

    @BeforeEach
    fun setUp() {
        getAllProjectLogsUseCase = mockk(relaxed = true)
        getUserByIdUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        executor = mockk(relaxed = true)
        handler = mockk(relaxed = true)

        projectLogsUi = ProjectLogsUi(
            getAllProjectLogsUseCase,
            getUserByIdUseCase,
            printer,
            executor,
            handler
        )
    }

    @Test
    fun `should call executor to try to execute`() = runTest {
        // Given
        val projectLog = createProjectLogHelper()
        val projectLogs = listOf(projectLog, projectLog)
        coEvery { getAllProjectLogsUseCase.getAllProjectLogs() } returns projectLogs

        val actionSlot = slot<suspend () -> List<Any>>()
        val successSlot = slot<suspend (List<Any>) -> Unit>()
        val errorSlot = slot<suspend (Throwable) -> Unit>()

        // When
        projectLogsUi.show()

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