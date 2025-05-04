package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectsForUserByIdUseCase
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
class ViewProjectForUserScreen(
    private val viewer: Viewer,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val getProjectsForUserById : GetProjectsForUserByIdUseCase
): UiScreen {
    override fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()
        val user = currentUserResult.getOrNull()

        if (user == null) {
            viewer.printError("No user found")
            return
        }else{
            val userProjectsResult = getProjectsForUserById.getProjectForUserById(user.id)
            userProjectsResult.fold(
              onSuccess = { projects ->
                  if (projects.isNotEmpty()) {
                      viewer.printTitle("Project For User: ${user.name}")
                      projects.forEachIndexed({ index, project ->
                          viewer.printInfoLine(
                              """
                              ${index + 1}.
                              -Name Project: ${project.name}
                              -Description: ${project.description}
                          """.trimIndent()
                          )
                      })
                  }else
                      viewer.printInfoLine("No project found for the current user.")
              } ,
                onFailure = {
                    viewer.printError("Failed to retrieve project logs: ${it.message}")
                }

            )
        }
    }
}