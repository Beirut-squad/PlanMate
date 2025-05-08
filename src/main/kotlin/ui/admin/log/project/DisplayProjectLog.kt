package org.example.ui.admin.log.project

import extensions.formatDateTime
import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.models.Project
import org.example.models.ProjectLog
import org.example.ui.common.components.Viewer
import java.util.*

open class DisplayProjectLog(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val viewer: Viewer,
) {
    private fun getUserName(userId: UUID): String? {
        return getUserByIdUseCase.getUser(userId).getOrThrow()?.name
    }

    protected fun displayProjectLog(index: Int, projectLog: ProjectLog) {
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

    private fun handleProjectCreation(index: Int, projectLog: ProjectLog) {
        val currentProject = projectLog.currentEntity
        val userName = getUserName(projectLog.userId)
        viewer.printCorrectOutput(
            "${index + 1}. User $userName created new project ${currentProject?.name} at ${currentProject?.createdAt?.formatDateTime()}"
        )
    }

    private fun handleProjectDeletion(index: Int, projectLog: ProjectLog) {
        val previousProject = projectLog.previousEntity
        val userName = getUserName(projectLog.userId)
        viewer.printCorrectOutput(
            "${index + 1}. User $userName deleted project ${previousProject?.name} at ${previousProject?.updatedAt?.formatDateTime()}"
        )
    }

    private fun handleProjectChanges(index: Int, projectLog: ProjectLog) {
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
        val projectName = currentProject?.name
        if (previousProject?.name != currentProject?.name) {
            viewer.printCorrectOutput(
                "${index + 1}. User $userName changed project $projectName name from ${previousProject?.name} to ${currentProject?.name} at ${currentProject?.updatedAt?.formatDateTime()}"
            )
        }
    }

    private fun handleDescriptionChange(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val projectName = currentProject?.name
        if (previousProject?.description != currentProject?.description) {
            viewer.printCorrectOutput(
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
            previousStates.size > currentStates.size -> handleStateRemoval(
                index,
                userName,
                previousProject,
                currentProject,
            )
        }
    }

    private fun handleStateAddition(
        index: Int, userName: String?, currentProject: Project?
    ) {
        val projectName = currentProject?.name
        viewer.printCorrectOutput(
            "${index + 1}. User $userName added new state ${currentProject?.state?.last()?.name} to project $projectName at ${currentProject?.updatedAt?.formatDateTime()}"
        )
    }

    private fun handleStateRemoval(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val projectName = currentProject?.name
        val deletedState = (previousProject?.state.orEmpty() - currentProject?.state.orEmpty().toSet()).first()
        viewer.printCorrectOutput(
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
            previousStates.size > currentStates.size -> handleAssignedUserRemoval(
                index,
                userName,
                previousProject,
                currentProject,
            )
        }
    }

    private fun handleAssignedUserAddition(
        index: Int, userName: String?, currentProject: Project?
    ) {
        val projectName = currentProject?.name
        viewer.printCorrectOutput(
            "${index + 1}. User $userName assigned user ${
                currentProject?.users.orEmpty().last().name
            } to project $projectName at ${currentProject?.updatedAt?.formatDateTime()}"
        )
    }

    private fun handleAssignedUserRemoval(
        index: Int, userName: String?, previousProject: Project?, currentProject: Project?
    ) {
        val projectName = currentProject?.name
        val deletedUser = (previousProject?.users.orEmpty() - currentProject?.users.orEmpty().toSet()).first()
        viewer.printCorrectOutput(
            "${index + 1}. User $userName unassigned user ${deletedUser} from project $projectName at ${currentProject?.updatedAt?.formatDateTime()}"
        )
    }
    //endregion

}