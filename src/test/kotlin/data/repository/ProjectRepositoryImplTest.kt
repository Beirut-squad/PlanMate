package data.repository


import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import data.datasource.interfaces.ProjectDataSource
import domain.model.Role
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*


class ProjectRepositoryImplTest {
    private val projectDataSource: ProjectDataSource = mockk()
    private lateinit var projectRepository: ProjectRepositoryImpl
    private val testProject = createProjectHelper()

    @BeforeEach
    fun setup() {
        projectRepository = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `test create project`() = runTest {
        val project = createProjectHelper()
        coEvery { projectDataSource.createProject(project) } returns Unit

        projectRepository.createProject(project)

        coVerify { projectDataSource.createProject(project) }
    }

    @Test
    fun `test edit project`() = runTest {
        val project = createProjectHelper()
        coEvery { projectDataSource.editProject(project) } returns Unit

        projectRepository.editProject(project)

        coVerify { projectDataSource.editProject(project) }
    }

    @Test
    fun `test delete project`() = runTest {
        val projectId = UUID.randomUUID()
        coEvery { projectDataSource.deleteProject(projectId) } returns Unit

        projectRepository.deleteProject(projectId)

        coVerify { projectDataSource.deleteProject(projectId) }
    }

    @Test
    fun `test get all projects`() = runTest {
        val projects = listOf(
            createProjectHelper(id = UUID.randomUUID(), name = "Project 1"),
            createProjectHelper(id = UUID.randomUUID(), name = "Project 2")
        )
        coEvery { projectDataSource.getAllProjects() } returns projects

        val result = projectRepository.getAllProjects()

        assertEquals(projects, result)
        coVerify { projectDataSource.getAllProjects() }
    }

    @Test
    fun `test get project by id`() = runTest {
        val projectId = UUID.randomUUID()
        val project = createProjectHelper()
        coEvery { projectDataSource.getProject(projectId) } returns project

        val result = projectRepository.getProject(projectId)

        assertEquals(project, result)
        coVerify { projectDataSource.getProject(projectId) }
    }

    @Test
    fun `test add state to project`() = runTest {
        val projectId = UUID.randomUUID()
        val state = createStateHelper()
        val project = createProjectHelper()
        coEvery { projectDataSource.addState(projectId, state) } returns project

        val result = projectRepository.addStateToProject(projectId, state)

        assertEquals(project, result)
        coVerify { projectDataSource.addState(projectId, state) }
    }

    @Test
    fun `test edit state in project`() = runTest {
        val projectId = UUID.randomUUID()
        val state = createStateHelper()
        val project = testProject
        coEvery { projectDataSource.editState(projectId, state) } returns project

        val result = projectRepository.editStateToProject(projectId, state)

        assertEquals(project, result)
        coVerify { projectDataSource.editState(projectId, state) }
    }

    @Test
    fun `test remove state from project`() = runTest {
        val projectId = UUID.randomUUID()
        val state = createStateHelper()
        val project = testProject
        coEvery { projectDataSource.deleteState(projectId, state) } returns project

        val result = projectRepository.removeStateFromProject(projectId, state)

        assertEquals(project, result)
        coVerify { projectDataSource.deleteState(projectId, state) }
    }

    @Test
    fun `test add mate to project`() = runTest {
        val projectId = UUID.randomUUID()
        val user = createUserHelper(role = Role.MATE)
        val project = testProject
        coEvery { projectDataSource.addMate(projectId, user) } returns project

        val result = projectRepository.addMateToProject(projectId, user)

        assertEquals(project, result)
        coVerify { projectDataSource.addMate(projectId, user) }
    }

    @Test
    fun `test get projects for user by id`() = runTest {
        val userId = UUID.randomUUID()
        val projects = listOf(
            createProjectHelper(id = UUID.randomUUID(), name = "Project 1"),
            createProjectHelper(id = UUID.randomUUID(), name = "Project 2"),

        )
        coEvery { projectDataSource.getUserProjectsById(userId) } returns projects

        val result = projectRepository.getProjectsForUserById(userId)

        assertEquals(projects, result)
        coVerify { projectDataSource.getUserProjectsById(userId) }
    }
}