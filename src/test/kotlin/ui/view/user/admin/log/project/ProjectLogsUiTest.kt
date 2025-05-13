package ui.view.user.admin.log.project

import creator_helper.createProjectLogHelper
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.use_case.authentication.GetUserByIdUseCase
import domain.use_case.log.GetAllProjectLogsUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.components.Printer

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
    fun `should call executer to try to exceute `() = runTest{
        // Given
        val projectLog = createProjectLogHelper()
        val projectLogs = listOf(projectLog, projectLog)
        coEvery{ getAllProjectLogsUseCase.getAllProjectLogs() } returns projectLogs

        // When
        projectLogsUi.show()

        // Then
        coVerify { executor.tryToExecute(
            action = {getAllProjectLogsUseCase.getAllProjectLogs()},
            onSuccess = { projectLogs ->
                projectLogs.forEachIndexed { index, projectLog ->
                    projectLogsUi.displayProjectLog(index, projectLog)
                }
            },
            onError = {handler.printHandledError(any())}
        ) }

    }
}