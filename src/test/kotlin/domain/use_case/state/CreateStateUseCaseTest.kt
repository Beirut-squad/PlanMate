package domain.use_case.state

import creator_helper.*
import domain.exception.EmptyStateNameException
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.UUID

class CreateStateUseCaseTest {
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
            addProjectStateUseCase.addStateToProject(currentUserID = user.id, project = project, state = result)
        }
    }


    @Test
    fun `createState should throw EmptyStateNameException when name is blank`() = runTest {
        // Given
        val project = createProjectHelper()

        // When & Then
        val exception = assertThrows<EmptyStateNameException> {
            createStateUseCase.createState("   ", project)
        }

        coVerify { getCurrentUserUseCase wasNot Called }
        coVerify { addProjectStateUseCase wasNot Called }
    }




}