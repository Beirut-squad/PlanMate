package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.exceptions.project_magement_exceptions.ProjectNotGetAllProjectsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class GetAllProjectsUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase = mockk(relaxed = true)
    private var getAllProjectsUseCase: GetAllProjectsUseCases = mockk(relaxed = true)
    private val createdUserId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        getAllProjectsUseCase = GetAllProjectsUseCases(
            projectRepository = projectRepository,
        )
    }

    @Test
    fun `should return all projects when call getAllProjects fun`() {
        //Given
        val projects = listOf(createProjectHelper(), createProjectHelper())
        getAllProjectsUseCase.getAllProjects()
        every { projectRepository.getAllProjects() } returns Result.success(projects)
        every { getAllProjectLogsUseCase.getAllProjectLogs() } returns Result.success(emptyList())

        //When
        val result = projectRepository.getAllProjects()

        //Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        verify { projectRepository.getAllProjects() }
        verify { getAllProjectLogsUseCase.getAllProjectLogs() }

    }

    @Test
    fun `getAllProjects should throw exception when repository fails`() {
        // Given

        val exception = ProjectNotGetAllProjectsException("Failed to retrieve tasks")
        every { projectRepository.getAllProjects() } returns Result.failure(exception)

        // Then
        verify(exactly = 0) { projectRepository.getAllProjects() }
        verify(exactly = 0) { getAllProjectLogsUseCase.getAllProjectLogs() }
    }

}