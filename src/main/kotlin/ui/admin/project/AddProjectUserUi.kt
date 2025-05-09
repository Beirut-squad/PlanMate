package org.example.ui.admin.project

import domain.use_case.authentication.GetAllUsersUseCase
import domain.use_case.project.AddProjectMateUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class AddProjectUserUi(
    private val projectId: UUID,
) :UiScreen,KoinComponent {
    private val printer : Printer by inject()
    private val reader : Reader by inject()
    private val  addMateToProjectUseCase:AddProjectMateUseCase by inject()
    private val getAllUsersUseCase: GetAllUsersUseCase by inject()
    override suspend fun show() {
        val users = getAllUsersUseCase.getUsers()

        printer.printTitle("Add User")
        if (users.isEmpty()) {
            printer.printInfoLine("No users available to add.")
            return
        }

        users.forEachIndexed { index, user ->
            printer.printPlainText("These are all the registered users:")
            printer.printInfoLine(
                "${index + 1}. " +
                        user.name
            )
        }

        printer.printInfoLine("Enter the number of the user to add (Enter anything else to cancel):")
        val selected = reader.readInt()

        if (selected == null || selected !in 1..users.size) {
            printer.printGoodbyeMessage("Operation cancelled.")
            return
        }

        val selectedUser = users[selected - 1]
        try {
            addMateToProjectUseCase.addMateToProject(projectId, selectedUser)
            printer.printGoodbyeMessage("User added successfully")
        }catch (e:Exception){
            printer.printError("${e.message}")
        }
    }
}