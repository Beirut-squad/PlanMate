package ui.admin.log.project

import creator_helper.createProjectHelper
import creator_helper.createProjectLogHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import extensions.formatDateTime
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.ui.admin.log.project.DisplayProjectLog
import org.example.ui.common.components.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DisplayProjectLogTest {

    private lateinit var displayProjectLog: DisplayProjectLog
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var viewer: Viewer


    @BeforeEach
    fun setup() {
        getUserByIdUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        displayProjectLog = DisplayProjectLog(getUserByIdUseCase, viewer)
    }

    @Test
    fun `should  view project log creation if project previous entity not found && current entity is found`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val currentProject = createProjectHelper()
            val projectLog = createProjectLogHelper(currentEntity = currentProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            verify {
                viewer.printCorrectOutput(
                    "${index + 1}. User $userName created new project ${currentProject?.name} at ${currentProject?.createdAt?.formatDateTime()}"
                )
            }
        }
    }

    @Test
    fun `should view project log deletion if project previous entity is found && current entity is not found`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val previousProject = createProjectHelper()
            val projectLog = createProjectLogHelper(previousEntity = previousProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            viewer.printCorrectOutput(
                "${index + 1}. User $userName deleted project ${previousProject?.name} at ${previousProject?.updatedAt?.formatDateTime()}"
            )
        }
    }

    @Test
    fun `should print name change if the name has changed`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val currentProject = createProjectHelper(name = "p1")
            val previousProject = createProjectHelper(name = "p2")
            val projectLog = createProjectLogHelper(previousEntity = previousProject , currentEntity = currentProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            verify {
                viewer.printCorrectOutput(
                    "${index + 1}. User $userName changed project ${currentProject?.name} name from ${previousProject?.name} to ${currentProject?.name} at ${currentProject?.updatedAt?.formatDateTime()}"
                )
            }
        }
    }


    @Test
    fun `should print Description change if the Description has changed`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val currentProject = createProjectHelper(description = "p1")
            val previousProject = createProjectHelper(description = "p2")
            val projectLog = createProjectLogHelper(previousEntity = previousProject , currentEntity = currentProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            verify {
                viewer.printCorrectOutput(
                    "${index + 1}. User $userName changed project ${currentProject.name} description from ${previousProject?.description} to ${currentProject?.description} at ${currentProject?.updatedAt?.formatDateTime()}"
                )
            }
        }


    }


    @Test
    fun `should print state addition if a state has been added`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val state1 = createStateHelper(name = "s1")
            val state2 = createStateHelper(name = "s2")

            val currentStates = listOf(state1, state2)
            val previousState = listOf(state1)

            val currentProject = createProjectHelper(state = currentStates)
            val previousProject = createProjectHelper(state = previousState)

            val projectLog = createProjectLogHelper(previousEntity = previousProject , currentEntity = currentProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            verify {
                viewer.printCorrectOutput(
                    "${index + 1}. User $userName added new state ${currentProject?.state?.last()?.name} to project ${currentProject.name} at ${currentProject?.updatedAt?.formatDateTime()}"
                )
            }
        }
    }

    @Test
    fun `should print state deletion if a state has been removed`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val state1 = createStateHelper(name = "s1")
            val state2 = createStateHelper(name = "s2")

            val currentStates = listOf(state1)
            val previousStates = listOf(state1,state2)

            val currentProject = createProjectHelper(state = currentStates)
            val previousProject = createProjectHelper(state = previousStates)

            val projectLog = createProjectLogHelper(previousEntity = previousProject , currentEntity = currentProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            verify {
                viewer.printCorrectOutput(
                    "${index + 1}. User $userName deleted state ${state2.name} from project ${currentProject.name} at ${currentProject?.updatedAt?.formatDateTime()}"
                )
            }
        }
    }

    @Test
    fun `should print state edition if a state has been edited`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val state1 = createStateHelper(name = "s1")
            val state2 = createStateHelper(id = state1.id, name = "s2")
            val state3 = createStateHelper(name = "s3")

            val currentStates = listOf(state2,state3)
            val previousStates = listOf(state1,state3)

            val currentProject = createProjectHelper(state = currentStates)
            val previousProject = createProjectHelper(state = previousStates)

            val projectLog = createProjectLogHelper(previousEntity = previousProject , currentEntity = currentProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            verify {
                viewer.printCorrectOutput(
                    "${index + 1}. User $userName edited state form ${state1.name} to ${state2.name} project ${currentProject.name} at ${currentProject?.updatedAt?.formatDateTime()}"
                )
            }
        }
    }

    @Test
    fun `should print assigned user change if the assigned user has changed`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user

            val user1 = createUserHelper()
            val user2 = createUserHelper()

            // When
            val currentProject = createProjectHelper(users = listOf(user1,user2))
            val previousProject = createProjectHelper(users = listOf(user1))
            val projectLog = createProjectLogHelper(previousEntity = previousProject , currentEntity = currentProject)
            val index = 0
            val userName = user.name
            displayProjectLog.displayProjectLog(index, projectLog)

            // Then
            viewer.printCorrectOutput(
                "${index + 1}. User $userName assigned user ${
                    currentProject?.users.orEmpty().last().name
                } to project ${currentProject.name} at ${currentProject?.updatedAt?.formatDateTime()}"
            )
        }
    }
}




