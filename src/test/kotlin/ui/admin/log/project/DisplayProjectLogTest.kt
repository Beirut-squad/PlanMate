package ui.admin.log.project

import creator_helper.createProjectHelper
import creator_helper.createProjectLogHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import domain.model.Project
import domain.use_case.authentication.GetUserByIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.components.Printer
import ui.extensions.formatDateTime
import ui.view.user.admin.log.project.ProjectLogUi

class ProjectLogUiTest {

    private lateinit var projectLogUi: ProjectLogUi
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var printer: Printer


    @BeforeEach
    fun setup() {
        getUserByIdUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        projectLogUi = ProjectLogUi(getUserByIdUseCase, printer)
    }

    @Test
    fun `should view project log creation if project previous entity not found && current entity is found`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user


            // When
            val currentProject = createProjectHelper()
            val projectLog = createProjectLogHelper(currentEntity = currentProject)
            val index = 0
            val userName = user.name
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName created new project ${currentProject?.title} at ${currentProject?.createdAt?.formatDateTime()}"
                )
            }
        }
    }

//    @Test
//    fun `should not view project log creation if project previous entity found && current entity is found`() {
//        runTest {
//            // Given
//            val user = createUserHelper()
//            coEvery { getUserByIdUseCase.getUser(any()) } returns user
//
//
//            // When
//            val currentProject: Project? = null
//            val previousProject: Project? = null
//            val projectLog = createProjectLogHelper(previousEntity = previousProject,currentEntity = currentProject)
//            val index = 0
//            val userName = user.name
//            projectLogUi.displayProjectLog(index, projectLog)
//
//            // Then
//            verify(exactly = 0) {
//                printer.printCorrectOutput(
//                    "${index + 1}. User $userName created new project ${currentProject?.title} at ${currentProject?.createdAt?.formatDateTime()}"
//                )
//            }
//        }
//    }

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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            printer.printCorrectOutput(
                "${index + 1}. User $userName deleted project ${previousProject?.title} at ${previousProject?.updatedAt?.formatDateTime()}"
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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName changed project ${currentProject?.title} name from ${previousProject?.title} to ${currentProject?.title} at ${currentProject?.updatedAt?.formatDateTime()}"
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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName changed project ${currentProject.title} description from ${previousProject?.description} to ${currentProject?.description} at ${currentProject?.updatedAt?.formatDateTime()}"
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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName added new state ${currentProject?.states?.last()?.name} to project ${currentProject.title} at ${currentProject?.updatedAt?.formatDateTime()}"
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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName deleted state ${state2.name} from project ${currentProject.title} at ${currentProject?.updatedAt?.formatDateTime()}"
                )
            }
        }
    }

    @Test
    fun `  should print state deletion if a state has been removed`() {
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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName deleted state ${state2.name} from project ${currentProject.title} at ${currentProject?.updatedAt?.formatDateTime()}"
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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            verify {
                printer.printCorrectOutput(
                    "${index + 1}. User $userName edited state form ${state1.name} to ${state2.name} project ${currentProject.title} at ${currentProject?.updatedAt?.formatDateTime()}"
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
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            printer.printCorrectOutput(
                "${index + 1}. User $userName assigned user ${
                    currentProject?.users.orEmpty().last().name
                } to project ${currentProject.title} at ${currentProject?.updatedAt?.formatDateTime()}"
            )
        }
    }

    /** MORE TEST CASES **/
    @Test
    fun `should not print state change if states are same size but no changes`() {
        runTest {
            // Given
            val user = createUserHelper()
            coEvery { getUserByIdUseCase.getUser(any()) } returns user

            // When
            val state1 = createStateHelper(name = "s1")
            val state2 = createStateHelper(name = "s2")

            val currentStates = listOf(state1, state2)
            val previousStates = listOf(state1, state2)

            val currentProject = createProjectHelper(state = currentStates)
            val previousProject = createProjectHelper(state = previousStates)

            val projectLog = createProjectLogHelper(previousEntity = previousProject, currentEntity = currentProject)
            val index = 0
            projectLogUi.displayProjectLog(index, projectLog)

            // Then
            // Should not call printCorrectOutput with any state change message
            verify(exactly = 0) {
                printer.printCorrectOutput(
                    match { it.contains("edited state") || it.contains("added new state") || it.contains("deleted state") }
                )
            }
        }
    }

    @Test
    fun `test isDeletion with both entities null`() {
        runTest {
            // Given - Creating a ProjectLog with both entities null
            val projectLog = createProjectLogHelper(previousEntity = null, currentEntity = null)

            // When & Then - Just execute to cover the branch
            projectLogUi.displayProjectLog(0, projectLog)

            // Verify no output was printed (already verified in previous test, but here for clarity)
            verify(exactly = 0) {
                printer.printCorrectOutput(any())
            }
        }
    }

}




