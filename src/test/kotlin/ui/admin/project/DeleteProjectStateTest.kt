package ui.admin.project

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.example.logic.exceptions.ErrorHandler
import org.example.logic.exceptions.NullInputException
import org.example.logic.use_cases.state_usecase.DeleteStateUseCase
import org.example.models.State
import org.example.ui.admin.project.DeleteProjectState
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class DeleteProjectStateTest {
    private val mockErrorHandler: ErrorHandler = mockk(relaxed = true)
    private val mockViewer: Viewer = mockk(relaxed = true)
    private val mockReader: Reader = mockk()
    private val mockDeleteStateUseCase: DeleteStateUseCase = mockk()
    private val projectId = UUID.randomUUID()
    private val state = State(id = UUID.randomUUID(), name = "Test State")
    private lateinit var deleteProjectState: DeleteProjectState

    @BeforeEach
    fun setUp() {
        deleteProjectState = DeleteProjectState(
            errorHandler = mockErrorHandler,
            viewer = mockViewer,
            reader = mockReader,
            state = state,
            projectId = projectId,
            deleteStateUseCase = mockDeleteStateUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `show should display title with state name`() {
        // Given
        every { mockReader.readInput() } returns "N"

        // When
        deleteProjectState.show()

        // Then
        verify(exactly = 1) { mockViewer.printTitle("Confirm deletion of state 'Test State': Y/N") }
    }

    @Test
    fun `show should call deleteState when user inputs Y`() {
        // Given
        every { mockReader.readInput() } returns "Y"
        every { mockDeleteStateUseCase.deleteState(projectId, state) } returns Result.success(Unit)

        // When
        deleteProjectState.show()

        // Then
        verify(exactly = 1) { mockDeleteStateUseCase.deleteState(projectId, state) }
    }

    @Test
    fun `show should call deleteState when user inputs y (lowercase)`() {
        // Given
        every { mockReader.readInput() } returns "y"
        every { mockDeleteStateUseCase.deleteState(projectId, state) } returns Result.success(Unit)

        // When
        deleteProjectState.show()

        // Then
        verify(exactly = 1) { mockDeleteStateUseCase.deleteState(projectId, state) }
    }

    @Test
    fun `show should not call deleteState when user inputs N`() {
        // Given
        every { mockReader.readInput() } returns "N"

        // When
        deleteProjectState.show()

        // Then
        verify(exactly = 0) { mockDeleteStateUseCase.deleteState(any(), any()) }
    }

    @Test
    fun `show should throw NullInputException when user inputs anything other than Y or N`() {
        // Given
        every { mockReader.readInput() } returns "MAYBE"

        // When/Then
        assertThat(runCatching { deleteProjectState.show() }.exceptionOrNull())
            .isInstanceOf(NullInputException::class.java)
        verify(exactly = 0) { mockDeleteStateUseCase.deleteState(any(), any()) }
    }

    @Test
    fun `show should throw NullInputException when user inputs nothing (null)`() {
        // Given
        every { mockReader.readInput() } returns null

        // When/Then
        assertThat(runCatching { deleteProjectState.show() }.exceptionOrNull())
            .isInstanceOf(NullInputException::class.java)
        verify(exactly = 0) { mockDeleteStateUseCase.deleteState(any(), any()) }
    }

    @Test
    fun `deleteState should handle error when use case fails`() {
        // Given
        val exception = RuntimeException("Delete failed")
        every { mockReader.readInput() } returns "Y"
        every { mockDeleteStateUseCase.deleteState(projectId, state) } returns Result.failure(exception)

        // When
        deleteProjectState.show()

        // Then
        verify(exactly = 1) { mockErrorHandler.handle(exception) }
    }

    @Test
    fun `deleteState should not handle error when use case succeeds`() {
        // Given
        every { mockReader.readInput() } returns "Y"
        every { mockDeleteStateUseCase.deleteState(projectId, state) } returns Result.success(Unit)

        // When
        deleteProjectState.show()

        // Then
        verify(exactly = 0) { mockErrorHandler.handle(any()) }
    }

}