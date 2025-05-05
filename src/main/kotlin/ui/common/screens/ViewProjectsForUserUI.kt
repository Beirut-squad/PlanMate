package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.project_manegment.GetProjectsForUserByIdUseCase
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class ViewProjectsForUserUI(
    private val viewer: Viewer,
    private val reader: Reader,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val getProjectsForUserById: GetProjectsForUserByIdUseCase,
    private val getProjectByIdUserCase: GetProjectByIdUseCase,
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId,
    private val createTaskUseCase: CreateTaskUseCase
) : UiScreen {

    override fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()
        val user = currentUserResult.getOrNull()

        if (user == null) {
            viewer.printError("No user found")
            return
        } else {
            val userProjectsResult = getProjectsForUserById.getProjectForUserById(user.id)
            userProjectsResult.fold(
                onSuccess = { projects ->
                    if (projects.isNotEmpty()) {
                        viewer.printTitle("Project For User: ${user.name}")
                        projects.forEachIndexed({ index, project ->
                            viewer.printInfoLine(
                                """
                              ${index + 1}-Name Project: ${project.name}
                          """.trimIndent()
                            )

                        })
                        viewer.printTitle("Select a project to view details:")
                        val selectedIndex = viewer.readIntInput("Enter project number:")?.minus(1)
                        val selectedProjectId = projects[selectedIndex!!].id

                        ViewProjectForUserUI(
                            selectedProjectId,
                            viewer,
                            reader,
                            getTaskByStateIdAndProjectId,
                            getCurrentLoggedInUserUseCase,
                            createTaskUseCase,
                            getProjectByIdUserCase
                        ).show()
                    } else
                        viewer.printInfoLine("No project found for the current user.")
                },
                onFailure = {
                    viewer.printError("Failed to retrieve projects: ${it.message}")
                }

            )
        }
    }
}