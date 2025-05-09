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
import org.example.logic.use_cases.project_manegment.AddStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AddProjectFakeDataSourceWriterParserColumnIndexStateWriterParserColumnIndexTest {
    private lateinit var repository: ProjectRepository
    private lateinit var logUseCase: CreateProjectLogUseCase
    private lateinit var useCase: AddStateToProjectUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        logUseCase = mockk(relaxed = true)
        useCase = AddStateToProjectUseCase(repository, logUseCase)
    }

    //region Test cases for addStateToProject()
    @Test
    fun `addStateToProject should call addStateToProject from ProjectRepository exactly once`() {
        // Given
        val currentUser = createUserHelper()
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { repository.addStateToProject(project.id, newState) } returns Result.success(project)

        // When
        val result = useCase.addStateToProject(currentUser.id,project, newState)

        // Then
        verify(exactly = 1) {
            repository.addStateToProject(project.id, newState)
            logUseCase.createProjectLog(userId = currentUser.id, previousProject = project, currentProject = result.getOrNull())
        }
    }

    @Test
    fun `addStateToProject should return success with Unit when project exists and state is valid`() {
        // Given
        val currentUser = createUserHelper()
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { repository.addStateToProject(project.id, newState) } returns Result.success(project)

        // When
        val result = useCase.addStateToProject(currentUser.id, project, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `addStateToProject should return failure with BlankFieldsException when user enters empty state name`() {
        // Given
        val currentUser = createUserHelper()
        val project = createProjectHelper()
        val newState = createStateHelper(name = " ")

        // When
        val result = useCase.addStateToProject(currentUser.id, project, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is BlankFieldsException)
    }

    @Test
    fun `addStateToProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val currentUser = createUserHelper()
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { repository.addStateToProject(project.id, newState) } returns Result.failure(
            NoProjectFoundException()
        )

        // When
        val result = useCase.addStateToProject(currentUser.id, project, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `addStateToProject() should return failure with DuplicateStateException when state name exists`() {
        // Given
        val currentUser = createUserHelper()
        val stats = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(state = stats)
        val duplicateState = createStateHelper(name = "Start")
        every { repository.addStateToProject(project.id, duplicateState) } returns Result.failure(
            DuplicateStateException()
        )

        // When
        val result = useCase.addStateToProject(currentUser.id,project, duplicateState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception.message)
    }
    //endregion

}