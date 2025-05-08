package ui.admin.log.task

import extensions.formatDateTime
import org.example.logic.use_cases.authentication.GetUserByIdUseCase
import org.example.models.Task
import org.example.models.TaskLog
import org.example.ui.common.components.Viewer
import java.util.*

open class DisplayTaskLog(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val viewer: Viewer,
) {
    private suspend fun getUserName(userId: UUID): String? {
        return getUserByIdUseCase.getUser(userId)?.name
    }

    protected suspend fun displayTaskLog(index: Int, taskLog: TaskLog) {
        when {
            taskLog.isCreation() -> handleTaskCreation(index, taskLog)
            taskLog.isDeletion() -> handleTaskDeletion(index, taskLog)
            else -> handleTaskChanges(index, taskLog)
        }
    }

    private fun TaskLog.isCreation(): Boolean {
        return previousEntity == null && currentEntity != null
    }

    private fun TaskLog.isDeletion(): Boolean {
        return previousEntity != null && currentEntity == null
    }

    private suspend fun handleTaskCreation(index: Int, taskLog: TaskLog) {
        val currentTask = taskLog.currentEntity
        val userName = getUserName(taskLog.userId)
        viewer.printCorrectOutput(
            "${index + 1}. User $userName created new task ${currentTask?.title} at ${currentTask?.createdAt?.formatDateTime()}"
        )
    }

    private suspend fun handleTaskDeletion(index: Int, taskLog: TaskLog) {
        val previousTask = taskLog.previousEntity
        val userName = getUserName(taskLog.userId)
        viewer.printCorrectOutput(
            "${index + 1}. User $userName deleted task ${previousTask?.title} at ${previousTask?.updatedAt?.formatDateTime()}"
        )
    }

    private suspend fun handleTaskChanges(index: Int, taskLog: TaskLog) {
        val previousTask = taskLog.previousEntity
        val currentTask = taskLog.currentEntity
        val userName = getUserName(taskLog.userId)
        handleNameChange(index, userName, previousTask, currentTask)
        handleDescriptionChange(index, userName, previousTask, currentTask)
        handleStateChanges(index, userName, previousTask, currentTask)
    }

    private fun handleNameChange(
        index: Int, userName: String?, previousTask: Task?, currentTask: Task?
    ) {
        val taskName = currentTask?.title
        if (previousTask?.title != currentTask?.title) {
            viewer.printCorrectOutput(
                "${index + 1}. User $userName changed task $taskName name from ${previousTask?.title} to ${currentTask?.title} at ${currentTask?.updatedAt?.formatDateTime()}"
            )
        }
    }

    private fun handleDescriptionChange(
        index: Int, userName: String?, previousTask: Task?, currentTask: Task?
    ) {
        val taskName = currentTask?.title
        if (previousTask?.description != currentTask?.description) {
            viewer.printCorrectOutput(
                "${index + 1}. User $userName changed task $taskName description from ${previousTask?.description} to ${currentTask?.description} at ${currentTask?.updatedAt?.formatDateTime()}"
            )
        }
    }

    private fun handleStateChanges(
        index: Int, userName: String?, previousTask: Task?, currentTask: Task?
    ) {
        val taskName = currentTask?.title
        val previousStates = previousTask?.state
        val currentStates = currentTask?.state
        viewer.printCorrectOutput(
            "${index + 1}. User $userName changed state from ${previousStates?.name} to ${currentStates?.name} in task $taskName at ${currentTask?.updatedAt?.formatDateTime()}"
        )
    }
}
