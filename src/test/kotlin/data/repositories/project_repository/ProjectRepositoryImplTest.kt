package data.repositories.project_repository


import creator_helper.createProjectLogHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.repositories.project_repository.ProjectRepositoryImpl
import org.example.logic.exceptions.ProjectNotCreatedException
import org.example.models.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectRepositoryImplTest {
    private var logDataSource: LogDataSource = mockk(relaxed = true)
    private var projectDataSource: ProjectDataSource = mockk(relaxed = true)
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    private var project: Project = mockk()

    @BeforeEach
    fun setup() {
        projectRepositoryImpl = ProjectRepositoryImpl(
            logDataSource = logDataSource,
            projectDataSource = projectDataSource
        )
    }

    //CreateProject Test
    @Test
    fun `should return success when project creation succeeds`() {
        // Given
        every { projectDataSource.createProject(project) } returns
                Result.success("Project Created")
        // When
        val result = projectRepositoryImpl.createProject(project)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { projectDataSource.createProject(project) }
    }

}