package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.exceptions.BlankFieldsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegment.EditProjectUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditProjectUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    //private val logUseCase: CreateProjectLogUseCase = mockk(relaxed = true)
    private var editProjectUseCase: EditProjectUseCase = mockk(relaxed = true)
    private val creatorUserID: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `should edit project name only when newName is provided and newDescription is null`() {
        // Given
        val project = createProjectHelper()
        val newName = "xyz"
        val newDescription = "zyx"

        //every { projectRepository.editProject(project) } returns Result.success(project)

        // When
        editProjectUseCase.editProject(
            newProject = project,
            newName = newName,
            newDescription = newDescription,
            creatorUserID = creatorUserID
        )

        //Then
        project.name = newName
        project.description = newDescription
        verify(exactly = 1) {
            projectRepository.editProject(project)
        }

//        verify(exactly = 1) {
//            logUseCase.createProjectLog(
//                previousProject = match { it.name == newName },
//                currentProject = match { it.name == newName },
//                userId = creatorUserID
//            )
//        }

        assertEquals(newName, project.name)
        assertEquals(newDescription, project.description)
    }

    @Test
    fun `should throw exception when both newName and newDescription are null`() {
        // Given
        val project = createProjectHelper()

        // When/Then
        assertThrows<BlankFieldsException> {
            editProjectUseCase.editProject(
                newProject = project,
                newName = null,
                newDescription = null,
                creatorUserID = creatorUserID
            )
        }
    }

    @Test
    fun `should throw exception when both newName and newDescription are blank`() {
        // Given
        val project = createProjectHelper()

         //When & Then
        verify (exactly = 0) {
            editProjectUseCase.editProject(
                newProject = project,
                newName = "",
                newDescription = "",
                creatorUserID = creatorUserID
            )
        }

    }

    @Test
    fun `should throw exception when newName is null`() {
        // Given
        val project = createProjectHelper()

        // When/Then
        verify (exactly = 0){
            editProjectUseCase.editProject(
                newProject = project,
                newName = null,
                newDescription = "x",
                creatorUserID = creatorUserID
            )
        }
    }

    @Test
    fun `should throw exception when newDescription is null`() {
        // Given
        val project = createProjectHelper()

        // When/Then
        verify (exactly = 0) {
            editProjectUseCase.editProject(
                newProject = project,
                newName = "x",
                newDescription = null,
                creatorUserID = creatorUserID
            )
        }
    }

}