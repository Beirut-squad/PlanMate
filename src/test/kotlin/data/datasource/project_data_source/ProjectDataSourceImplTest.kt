package data.datasource.project_data_source

import FileName
import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.data.csv.CsvReader
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.datasource.project_data_source.ProjectDataSourceImpl
import org.example.logic.exceptions.NoProjectFoundException
import org.example.models.Project
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProjectDataSourceImplTest {

    private lateinit var reader: CsvReader<Project>
    private lateinit var dataSource: ProjectDataSource
    private val fileName = FileName.PROJECTS_FILE

    @BeforeEach
    fun setUp() {
        reader = mockk(relaxed = true)
        dataSource = ProjectDataSourceImpl(reader)
    }

    //region Test cases for getProject()
    @Test
    fun `getProject should call read from CsvReader exactly once`() {
        // Given
        val project = createProjectHelper()
        every { reader.read(fileName) } returns listOf(project, createProjectHelper())

        // When
        val result = dataSource.getProject(project.id)

        // Then
        verify(exactly = 1) {
            reader.read(fileName)
        }
    }

    @Test
    fun `getProject should return success with project when project exists`() {
        // Given
        val project = createProjectHelper()
        every { reader.read(fileName) } returns listOf(project, createProjectHelper())

        // When
        val result = dataSource.getProject(project.id)

        // Then
        assertTrue(result.isSuccess)
        val expectedProject = result.getOrNull()
        assertNotNull(expectedProject)
        assertThat(expectedProject?.id).isEqualTo(project.id)
        assertThat(expectedProject?.name).isEqualTo(project.name)
        assertThat(expectedProject?.description).isEqualTo(project.description)
        assertThat(expectedProject?.creatorUserID).isEqualTo(project.creatorUserID)
        assertThat(expectedProject?.createdAt).isEqualTo(project.createdAt)
        assertThat(expectedProject?.updatedAt).isEqualTo(project.updatedAt)
    }

    @Test
    fun `getProject should return failure when repository empty`() {
        // Given
        val project = createProjectHelper()
        every { reader.read(fileName) } returns emptyList()

        // When
        val result = dataSource.getProject(project.id)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoProjectFoundException)
    }

    @Test
    fun `getProject should handle multiple projects correctly`() {
        // Given
        val project = createProjectHelper()
        val project2 = createProjectHelper()
        every { reader.read(fileName) } returns listOf(project, project2)

        // When
        val result = dataSource.getProject(project.id)
        val result2 = dataSource.getProject(project2.id)

        // Then
        assertEquals(project, result.getOrNull())
        assertEquals(project2, result2.getOrNull())
    }


    @Test
    fun `getProject() should return failure with NoProjectFoundException when project not found`() {
        // Given
        val project = createProjectHelper()
        every { reader.read(fileName) } throws NoProjectFoundException()

        // When
        val result = dataSource.getProject(project.id)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NoProjectFoundException)
        assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception?.message)
    }
    //endregion

}