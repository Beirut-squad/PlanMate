package ui.admin.log.task

import domain.exception.handler.ExceptionHandler
import domain.model.Task
import domain.model.TaskLog
import domain.use_case.authentication.GetUserByIdUseCase
import org.example.ui.common.components.Printer
import org.example.ui.extensions.formatDateTime
import java.util.*

open class TaskLogUi(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val printer: Printer,
    private val exceptionHandler: ExceptionHandler,

    ) {
    private suspend fun getUserName(userId: UUID): String {
       return exceptionHandler.runSafely {
            getUserByIdUseCase.getUser(userId)
        }.getOrThrow().name
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
        printer.printCorrectOutput(
            "${index + 1}. User $userName created new task ${currentTask?.title} at ${currentTask?.createdAt?.formatDateTime()}"
        )
    }

    private suspend fun handleTaskDeletion(index: Int, taskLog: TaskLog) {
        val previousTask = taskLog.previousEntity
        val userName = getUserName(taskLog.userId)
        printer.printCorrectOutput(
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
            printer.printCorrectOutput(
                "${index + 1}. User $userName changed task $taskName name from ${previousTask?.title} to ${currentTask?.title} at ${currentTask?.updatedAt?.formatDateTime()}"
            )
        }
    }

    private fun handleDescriptionChange(
        index: Int, userName: String?, previousTask: Task?, currentTask: Task?
    ) {
        val taskName = currentTask?.title
        if (previousTask?.description != currentTask?.description) {
            printer.printCorrectOutput(
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
        printer.printCorrectOutput(
            "${index + 1}. User $userName changed state from ${previousStates?.name} to ${currentStates?.name} in task $taskName at ${currentTask?.updatedAt?.formatDateTime()}"
        )
    }
}
