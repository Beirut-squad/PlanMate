package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.use_cases.log.CreateProjectLogUseCase
import org.example.ui.constants.StringConstants
import org.example.logic.exceptions.project_magement_exceptions.BlankFieldsException
import org.example.logic.exceptions.project_magement_exceptions.DuplicateStateException
import org.example.logic.exceptions.project_magement_exceptions.NoProjectFoundException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.project_manegment.EditStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EditProjectFakeDataSourceWriterParserColumnIndexUiStateWriterParserColumnIndexTest {
    private lateinit var repository: ProjectRepository
    private lateinit var logUseCase: CreateProjectLogUseCase
    private lateinit var useCase: EditStateToProjectUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        logUseCase = mockk(relaxed = true)
        useCase = EditStateToProjectUseCase(repository, logUseCase)
    }


    //region Test cases for editStateToProject()
    @Test
    fun `editStateToProject should call addStateToProject from ProjectRepository exactly once`() {
        // Given
        val currentUser = createUserHelper()
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { repository.editStateToProject(project.id, newState) } returns Result.success(project)

        // When
        val result = useCase.editStateToProject(currentUser.id, project, newState)

        // Then
        verify(exactly = 1) {
            repository.editStateToProject(project.id, newState)
            logUseCase.createProjectLog(userId = currentUser.id, previousProject = project, currentProject = result.getOrNull())
        }
    }

    @Test
    fun `editStateToProject should return success with Unit when project exists and state is updated`() {
        // Given
        val currentUser = createUserHelper()
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { repository.editStateToProject(project.id, newState) } returns Result.success(project)

        // When
        val result = useCase.editStateToProject(currentUser.id, project, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `editStateToProject should return failure with BlankFieldsException when user enters empty state name`() {
        // Given
        val currentUser = createUserHelper()
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = " "

        // When
        val result = useCase.editStateToProject(currentUser.id, project, newState)

        // Then
        kotlin.test.assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        kotlin.test.assertTrue(exception is BlankFieldsException)
    }


    @Test
    fun `editStateToProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val currentUser = createUserHelper()
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "Done"
        every { repository.editStateToProject(project.id, newState) } returns Result.failure(
            NoProjectFoundException()
        )

        // When
        val result = useCase.editStateToProject(currentUser.id, project, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }

    @Test
    fun `editStateToProject should return failure with DuplicateStateException when state is duplicate`() {
        // Given
        val currentUser = createUserHelper()
        val oldState = createStateHelper(name = "To Do")
        val project = createProjectHelper(state = listOf(oldState))
        val newState = oldState
        newState.name = "To Do"
        every { repository.editStateToProject(project.id, newState) } returns Result.failure(
            DuplicateStateException()
        )

        // When
        val result = useCase.editStateToProject(currentUser.id, project, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception?.message)
    }
    //endregion

}