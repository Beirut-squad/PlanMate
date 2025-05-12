package org.example.ui.admin.project

import domain.exception.handler.SafeExecutor
import domain.model.User
import domain.use_case.authentication.GetAllUsersUseCase
import domain.use_case.project.AddProjectMateUseCase
import org.example.core.domain.exception.handler.ExceptionHandler
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class AddProjectUserUi(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val addMateToProjectUseCase: AddProjectMateUseCase by inject()
    private val getAllUsersUseCase: GetAllUsersUseCase by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    override suspend fun show() {
        executor.tryToExecute(
            action = {
                getAllUsersUseCase.getUsers()
            }, onSuccess = { users ->
                printer.printTitle("Add User")
                if (users.isEmpty()) {
                    printer.printInfoLine("No users available to add.")
                    return@tryToExecute
                }
                showAllUsers(users)
                handleUserAddition(users)
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }

    private fun showAllUsers(users: List<User>) {
        users.forEachIndexed { index, user ->
            printer.printPlainText("These are all the registered users:")
            printer.printInfoLine(
                "${index + 1}. " + user.name
            )
        }
    }

    private suspend fun handleUserAddition(users: List<User>) {
        printer.printInfoLine("Enter the number of the user to add (Enter anything else to cancel):")
        val selected = reader.readInt()

        if (selected == null || selected !in 1..users.size) {
            printer.printGoodbyeMessage("Operation cancelled.")
            return
        }

        val selectedUser = users[selected - 1]
        executor.tryToExecute(
            action = {
                addMateToProjectUseCase.addMateToProject(projectId, selectedUser)
            },
            onSuccess = {
                printer.printGoodbyeMessage("User added successfully")
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }
}