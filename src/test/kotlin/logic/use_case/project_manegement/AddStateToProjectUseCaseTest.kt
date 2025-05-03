package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.logic.exceptions.DuplicateStateException
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegment.AddStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AddStateToProjectUseCaseTest {
    private lateinit var repository: ProjectRepository
    private lateinit var useCase: AddStateToProjectUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = AddStateToProjectUseCase(repository)
    }

    //region Test cases for addStateToProject()
    @Test
    fun `addStateToProject should call addStateToProject from ProjectRepository exactly once`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { repository.addStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = useCase.addStateToProject(project.id, newState)

        // Then
        verify(exactly = 1) {
            repository.addStateToProject(project.id, newState)
        }
    }

    @Test
    fun `addStateToProject should return success with Unit when project exists and state is valid`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { repository.addStateToProject(project.id, newState) } returns Result.success(Unit)

        // When
        val result = useCase.addStateToProject(project.id, newState)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `addStateToProject should return failure with NoProjectFoundException when project does not exist`() {
        // Given
        val project = createProjectHelper()
        val newState = createStateHelper()
        every { repository.addStateToProject(project.id, newState) } returns Result.failure(
            NoProjectFoundException()
        )

        // When
        val result = useCase.addStateToProject(project.id, newState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }

    @Test
    fun `addStateToProject() should return failure with DuplicateStateException when state name exists`() {
        // Given
        val stats = listOf(createStateHelper(name = "Start"), createStateHelper(name = "To Do"))
        val project = createProjectHelper(state = stats)
        val duplicateState = createStateHelper(name = "Start")
        every { repository.addStateToProject(project.id, duplicateState) } returns Result.failure(
            DuplicateStateException()
        )

        // When
        val result = useCase.addStateToProject(project.id, duplicateState)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DuplicateStateException)
        assertEquals(StringConstants.Project.DUPLICATE_STATE, exception.message)
    }
    //endregion

}