package logic.use_case.project_manegement

import io.mockk.mockk
import io.mockk.verify
import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.project_magement_exceptions.BlankFieldsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.project_manegment.CreateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

class CreateProjectUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private val logUseCase: CreateProjectLogUseCase = mockk(relaxed = true)
    private var createProjectUseCase: CreateProjectUseCase = mockk()
    private val creatorUserID = UUID.randomUUID()


    @BeforeEach
    fun setup() {
        createProjectUseCase = CreateProjectUseCase(projectRepository,logUseCase)
    }

    @Test
    fun `should create project successfully when call createProject function`() {
        // Given
        val name = "x"
        val description = "Test Description"
        val stateNames = listOf("Todo", "In Progress", "Done")

        // When
        createProjectUseCase.createProject(
            creatorUserID = creatorUserID,
            name = name,
            description = description,
            stateNames = stateNames,
        )

        // Then
        verify(exactly = 1) {
            projectRepository.createProject(
                match { project ->
                    project.name == name &&
                            project.description == description &&
                            project.creatorUserID == creatorUserID &&
                            project.state.map { it.name } == stateNames
                }
            )
        }
        verify { logUseCase.createProjectLog(any(), null, any()) }

    }

    @Test
    fun `should not create project successfully when name is blank`() {
        // Given
        val name = ""
        val description = "Test Description"
        val stateNames = listOf("Todo", "In Progress", "Done")

        // When
        val exception = assertThrows<BlankFieldsException> {
            createProjectUseCase.createProject(
                creatorUserID = creatorUserID,
                name = name,
                description = description,
                stateNames = stateNames,
            )
        }

        // Then
        assertEquals("You should write a valid input as a string.", exception.message)
        verify(exactly = 0) {projectRepository.createProject(any())  }
        verify(exactly = 0) { logUseCase.createProjectLog(creatorUserID,null,any()) }
    }

    @Test
    fun `should not create project successfully when description is blank`() {
        // Given
        val name = "Test Description"
        val description = ""
        val stateNames = listOf("Todo", "In Progress", "Done")

        // When
        val exception = assertThrows<BlankFieldsException> {
            createProjectUseCase.createProject(
                creatorUserID = creatorUserID,
                name = name,
                description = description,
                stateNames = stateNames,
            )
        }

        // Then
        assertEquals("You should write a valid input as a string.", exception.message)
        verify(exactly = 0) {projectRepository.createProject(any())  }
        verify(exactly = 0) { logUseCase.createProjectLog(creatorUserID,null,any()) }
    }

    @Test
    fun `should not create project successfully when name and description are blank`() {
        // Given
        val name = ""
        val description = ""
        val stateNames = listOf("Todo", "In Progress", "Done")

        // When
        val exception = assertThrows<BlankFieldsException> {
            createProjectUseCase.createProject(
                creatorUserID = creatorUserID,
                name = name,
                description = description,
                stateNames = stateNames,
            )
        }

        // Then
        assertEquals("You should write a valid input as a string.", exception.message)
        verify(exactly = 0) {projectRepository.createProject(any())  }
        verify(exactly = 0) { logUseCase.createProjectLog(creatorUserID,null,any()) }
    }

}