package data.repositories.project_repository


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.repositories.project_repository.ProjectRepositoryImpl
import org.example.logic.exceptions.ProjectNotCreatedException
import org.example.logic.exceptions.ProjectNotDeletedException
import org.example.logic.exceptions.ProjectNotEditedException
import org.example.models.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProjectRepositoryImplTest {
    private var projectDataSource: ProjectDataSource = mockk(relaxed = true)
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    private var project: Project = mockk()

    @BeforeEach
    fun setup() {
        projectRepositoryImpl = ProjectRepositoryImpl(
            projectDataSource = projectDataSource
        )
    }

    //CreateProject Test
    @Test
    fun `should return success when project creation succeeds`() {
        // Given
        every { projectDataSource.createProject(project) } returns
                Result.success("true")
        // When
        val result = projectRepositoryImpl.createProject(project)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { projectDataSource.createProject(project) }
    }

    @Test
    fun `should return failure when project creation fails to create`() {
        // Given
        val exception = ProjectNotCreatedException("Project could not be created")
        every { projectDataSource.createProject(project) } returns Result.failure(exception)

        // When
        val result = projectRepositoryImpl.createProject(project)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ProjectNotCreatedException)
        verify(exactly = 1) { projectDataSource.createProject(project) }
    }

    //editProject Test
    @Test
    fun `should return success when project edit succeeds`(){
        //Given
        every { projectDataSource.editProject(project) } returns
                Result.success("true")

        //When
        val result = projectRepositoryImpl.editProject(project)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { projectDataSource.editProject(project) }
    }

    @Test
    fun `should return failure when project edit fails `() {
        // Given
        val exception = ProjectNotEditedException("Project not edited yet")
        every { projectDataSource.editProject(project) } returns
                Result.failure(exception)

        // When
        val result = projectRepositoryImpl.editProject(project)

        // Then
        //assertTrue(result.isFailure)
        assertThrows<ProjectNotEditedException> { result.getOrThrow() }
        verify(exactly = 1) { projectDataSource.editProject(project) }
    }

    @Test
    fun `should return failure when project edited success and log failure`() {
        // Given
        every { projectDataSource.editProject(project) } returns
                Result.success("true")
        // When
        val result = projectRepositoryImpl.editProject(project)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { projectDataSource.editProject(project) }
    }

    //delete project
    @Test
    fun `should return success when project delete succeeds`(){
        //Given
        every { projectDataSource.deleteProject(project) } returns
                Result.success("true")

        //When
        val result = projectRepositoryImpl.deleteProject(project)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { projectDataSource.deleteProject(project) }
    }

    @Test
    fun `should return failure when project deleting fails `() {
        // Given
        val exception = ProjectNotDeletedException("Project not edited yet")
        every { projectDataSource.deleteProject(project) } returns
                Result.failure(exception)

        // When
        val result = projectRepositoryImpl.deleteProject(project)

        // Then
        //assertTrue(result.isFailure)
        assertThrows<ProjectNotDeletedException> { result.getOrThrow() }
        verify(exactly = 1) { projectDataSource.deleteProject(project) }
    }

}