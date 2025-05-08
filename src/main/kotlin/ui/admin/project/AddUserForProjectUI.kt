package org.example.ui.admin.project

import org.example.logic.use_cases.authentication.GetAllUsersUseCase
import org.example.logic.use_cases.project_manegment.AddMateToProjectUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class AddUserForProjectUI(
    private val projectId: UUID,
) :UiScreen,KoinComponent {
    private val viewer : Viewer by inject()
    private val reader : Reader by inject()
    private val  addMateToProjectUseCase:AddMateToProjectUseCase by inject()
    private val getAllUsersUseCase: GetAllUsersUseCase by inject()
    override suspend fun show() {
        val users = getAllUsersUseCase.getUsers()

        viewer.printTitle("Add User")
        if (users.isEmpty()) {
            viewer.printInfoLine("No users available to add.")
            return
        }

        users.forEachIndexed { index, user ->
            viewer.printPlainText("These are all the registered users:")
            viewer.printInfoLine(
                "${index + 1}. " +
                        user.name
            )
        }

        viewer.printInfoLine("Enter the number of the user to add (Enter anything else to cancel):")
        val selected = reader.readInt()

        if (selected == null || selected !in 1..users.size) {
            viewer.printGoodbyeMessage("Operation cancelled.")
            return
        }

        val selectedUser = users[selected - 1]
        try {
            addMateToProjectUseCase.addMateToProject(projectId, selectedUser)
            viewer.printGoodbyeMessage("User added successfully")
        }catch (e:Exception){
            viewer.printError("${e.message}")
        }
    }
}