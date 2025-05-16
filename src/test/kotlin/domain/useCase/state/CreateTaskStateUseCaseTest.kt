package domain.useCase.state

import creator_helper.createProjectHelper
import creator_helper.createUserHelper
import ui.common.exception.EmptyStateNameException
import domain.useCase.authentication.GetCurrentUserUseCase
import domain.useCase.project.AddProjectStateUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateTaskStateUseCaseTest {
    private val addProjectStateUseCase: AddProjectStateUseCase = mockk(relaxed = true)
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk(relaxed = true)
    private lateinit var createStateUseCase: CreateStateUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        createStateUseCase = CreateStateUseCase(addProjectStateUseCase, getCurrentUserUseCase)
    }


    @Test
    fun `createState should create and return a new state when input is valid`() = runTest {
        // Given
        val user = createUserHelper()
        val project = createProjectHelper()
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        // When
        val result = createStateUseCase.createState("Doing", project)

        // Then
        assertEquals("Doing", result.name)
        assertNotNull(result.id)
        coVerify(exactly = 1) {
            addProjectStateUseCase.addStateToProject(currentUserID = user.id, project = project, taskState = result)
        }
    }


    @Test
    fun `createState should throw EmptyStateNameException when name is blank`() = runTest {
        // Given
        val project = createProjectHelper()

        // When & Then
        assertThrows<EmptyStateNameException> {
            createStateUseCase.createState("   ", project)
        }

        coVerify { getCurrentUserUseCase wasNot Called }
        coVerify { addProjectStateUseCase wasNot Called }
    }


}