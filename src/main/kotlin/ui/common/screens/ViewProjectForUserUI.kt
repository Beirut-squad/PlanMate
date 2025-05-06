package org.example.ui.common.screens

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class ViewProjectForUserUI(
    private val projectId : UUID,
    ):UiScreen, KoinComponent {
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId by inject()
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val getProjectByIdUseCase :GetProjectByIdUseCase by inject()
    override fun show() {
        val projectResult = getProjectByIdUseCase.getProjectById(projectId)

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
                viewer.printOptions("View state for project", "View all task for project","Create new task", "" +
                        "Enter Any Thing To Go Back")

                val option = reader.readInt()
                when (option) {
                    1 -> {
                        ViewStateSelectedForProjectUI(
                            viewer,
                            project.id,
                            getProjectByIdUseCase,
                            getTaskByStateIdAndProjectId
                        ).show()
                    }
                    2 -> {
                        if (project.state.isEmpty()) {
                            viewer.printError("Cannot create a task because this project has no states. Please add a state first.")
                        } else {
                            ViewAllTaskForProjectUI(
                                project.id,
                            ).show()
                        }
                    }
                    3 -> {
                        if (project.state.isEmpty()) {
                            viewer.printError("Cannot create a task because this project has no states. Please add a state first.")
                        } else {
                            CreateNewTaskUI(projectId).show()
                        }
                    }
                    else -> {
                        viewer.printGoodbyeMessage("Goodbye")
                        ViewProjectsForUserUI().show()
                    }
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve project: ${it.message}")
            }
        )
    }
}