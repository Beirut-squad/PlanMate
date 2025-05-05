package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.UUID

class CreateNewTaskUI(
    private val viewer: Viewer,
    private val reader: Reader,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val projectId : UUID,
    private val createTaskUseCase: CreateTaskUseCase,
    private val getProjectByIdUseCase :GetProjectByIdUseCase

) : UiScreen {
    override fun show() {
        val selectedProject = getProjectByIdUseCase.getProjectById(projectId)
        selectedProject.fold(
            onSuccess = { selectedProject ->
                val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()
                val user = currentUserResult.getOrNull()

                if (user == null) {
                    viewer.printError("No user found")
                    return
                }

                viewer.printTitle("Let's create a task")

                viewer.printInfoLine("Write your task name:")
                val name = reader.readInput()?: ""

                viewer.printOptions("Tell me more about description of your task:")
                val description = reader.readInput()?: ""

                viewer.printOptions("Choose a state for the task:")
                selectedProject.state.forEachIndexed { index, state ->
                    viewer.printInfoLine("${index + 1}. ${state.name}")
                }

                val stateIndexInput = reader.readInput()
                val selectedStateIndex = stateIndexInput?.toIntOrNull()?.minus(1)

                if (selectedStateIndex == null || selectedStateIndex !in selectedProject.state.indices) {
                    viewer.printError("Invalid state selection.")
                    return
                }
                val selectedState = selectedProject.state[selectedStateIndex]
                createTaskUseCase.createTask(name,description,selectedState,selectedProject.id,user.id)

            },
            onFailure = {
                viewer.printError("Failed to retrieve project: ${it.message}")
            }
        )


    }
}