package org.example.ui.common.screens

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.UUID

class ViewProjectForUserScreen(
    private val ProjectId : UUID,
    private val viewer: Viewer,
    private val reader: Reader,
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val getProjectByIdUseCase :GetProjectByIdUseCase
    ):UiScreen {

    override fun show() {
        val projectResult = getProjectByIdUseCase.getProjectById(ProjectId)

        projectResult.fold(
            onSuccess = { project ->
                viewer.printInfoLine(
                    """
                - Project Name: ${project.name}
                - Description: ${project.description}
                - Created At: ${project.createdAt}
                """.trimIndent()
                )

                viewer.printInfoLine("Choose an option:")
                viewer.printOptions("View state for project", "Create new task", "Exit")

                val option = reader.readInt()
                when (option) {
                    1 -> {
                        ViewStateSelectedForProject(
                            viewer,
                            project.state,
                            project.id,
                            getTaskByStateIdAndProjectId
                        ).show()
                    }
                    2 -> {
                        CreateNewTaskUI(
                            viewer,
                            reader,
                            getCurrentLoggedInUserUseCase,
                            ProjectId,
                            createTaskUseCase,
                            getProjectByIdUseCase
                        ).show()
                    }
                    3 -> {
                        viewer.printGoodbyeMessage("Goodbye")
                    }
                    else -> viewer.printError("Invalid option.")
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve project: ${it.message}")
            }
        )
    }
}