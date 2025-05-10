package org.example.ui.admin.log.project

import domain.exception.handler.ExceptionHandler
import domain.model.Project
import domain.model.ProjectLog
import domain.model.State
import domain.use_case.authentication.GetUserByIdUseCase
import org.example.ui.common.components.Printer
import org.example.ui.extensions.formatDateTime
import java.util.*

open class ProjectLogUi(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
    private val exceptionHandler: ExceptionHandler,
) {
    private suspend fun getUserName(userId: UUID): String {
        return exceptionHandler.runSafely {
            getUserByIdUseCase.getUser(userId)
        }.getOrThrow().name
    }

    protected suspend fun displayProjectLog(index: Int, projectLog: ProjectLog) {
        when {
            projectLog.isCreation() -> handleProjectCreation(index, projectLog)
            projectLog.isDeletion() -> handleProjectDeletion(index, projectLog)
            else -> handleProjectChanges(index, projectLog)
        }
    }

    private fun ProjectLog.isCreation(): Boolean {
        return previousEntity == null && currentEntity != null
    }

    private fun ProjectLog.isDeletion(): Boolean {
        return previousEntity != null && currentEntity == null
    }

    private suspend fun handleProjectCreation(index: Int, projectLog: ProjectLog) {
        val currentProject = projectLog.currentEntity
        val userName = getUserName(projectLog.userId)
        printer.printCorrectOutput(
            "${index + 1}. User $userName created new project ${currentProject?.title} at ${currentProject?.createdAt?.formatDateTime()}"
        )
    }

    private suspend fun handleProjectDeletion(index: Int, projectLog: ProjectLog) {
        val previousProject = projectLog.previousEntity
        val userName = getUserName(projectLog.userId)
        printer.printCorrectOutput(
            "${index + 1}. User $userName deleted project ${previousProject?.title} at ${previousProject?.updatedAt?.formatDateTime()}"
        )
    }

    private suspend fun handleProjectChanges(index: Int, projectLog: ProjectLog) {
        val previousProject = projectLog.previousEntity
        val currentProject = projectLog.currentEntity
        val userName = getUserName(projectLog.userId)
        handleNameChange(index, userName, previousProject, currentProject)
        handleDescriptionChange(index, userName, previousProject, currentProject)
        handleStateChanges(index, userName, previousProject, currentProject)
        handleAssignedUserChange(index, userName, previousProject, currentProject)
    }

    private fun handleNameChange(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val projectName = currentProject?.title
        if (previousProject?.title != currentProject?.title) {
            printer.printCorrectOutput(
                "${index + 1}. User $userName changed project $projectName name from ${previousProject?.title} to ${currentProject?.title} at ${currentProject?.updatedAt?.formatDateTime()}"
            )
        }
    }

    private fun handleDescriptionChange(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val projectName = currentProject?.title
        if (previousProject?.description != currentProject?.description) {
            printer.printCorrectOutput(
                "${index + 1}. User $userName changed project $projectName description from ${previousProject?.description} to ${currentProject?.description} at ${currentProject?.updatedAt?.formatDateTime()}"
            )
        }
    }

    //region handleStateChanges
    private fun handleStateChanges(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val previousStates = previousProject?.state.orEmpty()
        val currentStates = currentProject?.state.orEmpty()
        when {
            previousStates.size < currentStates.size -> handleStateAddition(index, userName, currentProject)
            previousStates.size == currentStates.size -> handleStateEdition(
                index, userName, previousProject, currentProject,
            )

            previousStates.size > currentStates.size -> handleStateRemoval(
                index, userName, previousProject, currentProject,
            )
        }
    }

    private fun handleStateAddition(
        index: Int, userName: String?, currentProject: Project?
    ) {
        val projectName = currentProject?.title
        printer.printCorrectOutput(
            "${index + 1}. User $userName added new state ${currentProject?.state?.last()?.name} to project $projectName at ${currentProject?.updatedAt?.formatDateTime()}"
        )
    }

    private fun handleStateEdition(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val projectName = currentProject?.title
        val previousState = previousProject?.state.orEmpty()
        val currentState = currentProject?.state.orEmpty()
        findFirstStateChange(oldStates = previousState, updatedStates = currentState)?.let { (oldState, newState) ->
            printer.printCorrectOutput(
                "${index + 1}. User $userName edited state form ${oldState.name} to ${newState.name} project $projectName at ${currentProject?.updatedAt?.formatDateTime()}"
            )
        }
    }

    private fun findFirstStateChange(oldStates: List<State>, updatedStates: List<State>): Pair<State, State>? {
        updatedStates.forEach { updatedState ->
            oldStates.find { it.id == updatedState.id }?.let { oldState ->
                if (oldState.name != updatedState.name) return oldState to updatedState
            }
        }
        return null
    }

    private fun handleStateRemoval(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val projectName = currentProject?.title
        val deletedState = (previousProject?.state.orEmpty() - currentProject?.state.orEmpty().toSet()).first()
        printer.printCorrectOutput(
            "${index + 1}. User $userName deleted state ${deletedState.name} from project $projectName at ${currentProject?.updatedAt?.formatDateTime()}"
        )
    }
    //endregion

    //region handleAssignedUserChange
    private fun handleAssignedUserChange(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val previousStates = previousProject?.users.orEmpty()
        val currentStates = currentProject?.users.orEmpty()
        when {
            previousStates.size < currentStates.size -> handleAssignedUserAddition(index, userName, currentProject)
//            previousStates.size > currentStates.size -> handleAssignedUserRemoval(index, userName, previousProject, currentProject,)
        }
    }

    private fun handleAssignedUserAddition(
        index: Int, userName: String?, currentProject: Project?
    ) {
        val projectName = currentProject?.title
        printer.printCorrectOutput(
            "${index + 1}. User $userName assigned user ${
                currentProject?.users.orEmpty().last().name
            } to project $projectName at ${currentProject?.updatedAt?.formatDateTime()}"
        )
    }
}