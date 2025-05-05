package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.project_manegment.GetProjectsForUserByIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.UUID

class ViewProjectForUserScreen(
    private val projectId : UUID,
    private val viewer: Viewer,
    private val getProjectByIdUserCase : GetProjectByIdUseCase,
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId
):UiScreen {
    override fun show() {
       val projectResult = getProjectByIdUserCase.getProjectById(projectId)
        projectResult.fold(
            onSuccess = { project ->
                viewer.printInfoLine("""
                    -Project Name:${project.name}
                    -Description: ${project.description}
                    -Created At: ${project.createdAt}
                """.trimIndent())

                viewer.printTitle("Select a state to view details:")

                val allStates = project.state
                allStates.forEachIndexed { index, state ->
                    viewer.printInfoLine("${index + 1}. ${state.name}")
                }
                val choice = viewer.readIntInput("Enter the number of the state to view (0 to cancel): ")

                if (choice in 1..allStates.size) {
                    val selectedState = allStates.getOrNull(choice?.minus(1) ?: -1)
                    if (selectedState != null) {
                        val stateScreen = ViewStateSelectedForProject(viewer, selectedState,project.id,getTaskByStateIdAndProjectId)
                        stateScreen.show()
                    } else {
                        viewer.printError("Invalid state selected.")
                    }
                } else if (choice == 0) {
                    viewer.printInfoLine("Cancelled.")
                } else {
                    viewer.printError("Invalid choice. Please enter a number between 1 and ${allStates.size}.")
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve project: ${it.message}")

            }
        )
    }
}