package org.example.ui.admin.log

import extensions.formatDateTime
import org.example.logic.exceptions.ErrorHandler
import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.example.models.Project
import org.example.models.ProjectLog
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.UUID
//
//class AllProjectsLogsView(
//    private val getAllProjectLogsUseCase: GetAllProjectLogsUseCase,
//    private val getUserByIdUseCase: GetUserByIdUseCase,
//    private val viewer: Viewer,
//) : UiScreen {
//
//    override fun show() {
//        getAllProjectLogsUseCase.getAllProjectLogs().fold(onFailure = {
//            ErrorHandler().handle(it)
//        }, onSuccess = { projectLogs ->
//            if (projectLogs.isEmpty()) {
//                viewer.printError("No project logs found")
//                return@fold
//            } else {
//                projectLogs.forEachIndexed { index, projectLog ->
//                    displayProjectLog(index, projectLog)
//                }
//            }
//        })
//    }
//
//    private fun getUserName(userId: UUID): String? {
//        return getUserByIdUseCase.getUser(userId).getOrThrow()?.name
//    }
//
//    private fun displayProjectLog(index: Int, projectLog: ProjectLog) {
//        when {
//            projectLog.isCreation() -> handleProjectCreation(index, projectLog)
//            projectLog.isDeletion() -> handleProjectDeletion(index, projectLog)
//            else -> handleProjectChanges(index, projectLog)
//        }
//    }
//
//    private fun ProjectLog.isCreation(): Boolean {
//        return previousEntity == null && currentEntity != null
//    }
//
//    private fun ProjectLog.isDeletion(): Boolean {
//        return previousEntity != null && currentEntity == null
//    }
//
//    private fun handleProjectCreation(index: Int, projectLog: ProjectLog) {
//        val currentProject = projectLog.currentEntity
//        val userName = getUserName(projectLog.userId)
//        viewer.printCorrectOutput(
//            "${index + 1}. User $userName created new project ${currentProject?.name} at ${currentProject?.createdAt?.formatDateTime()}"
//        )
//    }
//
//    private fun handleProjectDeletion(index: Int, projectLog: ProjectLog) {
//        val previousProject = projectLog.previousEntity
//        val userName = getUserName(projectLog.userId)
//        viewer.printCorrectOutput(
//            "${index + 1}. User $userName deleted project ${previousProject?.id} at ${previousProject?.updatedAt?.formatDateTime()}"
//        )
//    }
//
//    private fun handleProjectChanges(index: Int, projectLog: ProjectLog) {
//        val previousProject = projectLog.previousEntity
//        val currentProject = projectLog.currentEntity
//        val userName = getUserName(projectLog.userId)
//        val projectId = currentProject?.id ?: return
//        handleNameChange(index, userName, previousProject, currentProject, projectId)
//        handleDescriptionChange(index, userName, previousProject, currentProject, projectId)
//        handleStateChanges(index, userName, previousProject, currentProject, projectId)
//    }
//
//    private fun handleNameChange(
//        index: Int, userName: String?, previousProject: Project?, currentProject: Project?, projectId: UUID
//    ) {
//        if (previousProject?.name != currentProject?.name) {
//            viewer.printCorrectOutput(
//                "${index + 1}. User $userName changed project $projectId name from ${previousProject?.name} to ${currentProject?.name} at ${currentProject?.updatedAt?.formatDateTime()}"
//            )
//        }
//    }
//
//    private fun handleDescriptionChange(
//        index: Int, userName: String?, previousProject: Project?, currentProject: Project?, projectId: UUID
//    ) {
//        if (previousProject?.description != currentProject?.description) {
//            viewer.printCorrectOutput(
//                "${index + 1}. User $userName changed project $projectId description from ${previousProject?.description} to ${currentProject?.description} at ${currentProject?.updatedAt?.formatDateTime()}"
//            )
//        }
//    }
//
//    private fun handleStateChanges(
//        index: Int, userName: String?, previousProject: Project?, currentProject: Project?, projectId: UUID
//    ) {
//        val previousStates = previousProject?.state.orEmpty()
//        val currentStates = currentProject?.state.orEmpty()
//        when {
//            previousStates.size < currentStates.size -> handleStateAddition(index, userName, currentProject, projectId)
//            previousStates.size > currentStates.size -> handleStateRemoval(index, userName, previousProject, currentProject, projectId)
//        }
//    }
//
//    private fun handleStateAddition(
//        index: Int, userName: String?, currentProject: Project?, projectId: UUID
//    ) {
//        viewer.printCorrectOutput(
//            "${index + 1}. User $userName added new state ${currentProject?.state?.last()?.name} to project $projectId at ${currentProject?.updatedAt?.formatDateTime()}"
//        )
//    }
//
//    private fun handleStateRemoval(
//        index: Int, userName: String?, previousProject: Project?, currentProject: Project?, projectId: UUID
//    ) {
//        val deletedState = (previousProject?.state.orEmpty() - currentProject?.state.orEmpty().toSet()).first()
//        viewer.printCorrectOutput(
//            "${index + 1}. User $userName deleted state ${deletedState.name} from project $projectId at ${currentProject?.updatedAt?.formatDateTime()}"
//        )
//    }
//
//}