package org.example.ui.common.project

import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.example.ui.common.task.CreateNewTaskUi
import org.example.ui.mate.ViewProjectsForUserUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class ViewProjectForMateUi(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    override suspend fun show() {
        try {
            var running = true
            while (running) {
                val project = getProjectByIdUseCase.getProjectById(projectId)
                viewer.printInfoLine(
                    """
                - Project Name: ${project.name}
                - Description: ${project.description}
                - Created At: ${project.createdAt}
                """.trimIndent()
                )

                viewer.printInfoLine("Choose an option:")
                viewer.printOptions(
                    "View state for project", "View all task for project", "Create new task", "" +
                            "Enter Any Thing To Go Back"
                )

                val option = reader.readInt()
                when (option) {
                    1 -> {
                        ViewStateSelectedForProjectUi(
                            project.id,
                        ).show()
                    }

                    2 -> {
                        ViewAllTaskForProjectUi(
                            project.id
                        ).show()
                    }

                    3 -> {
                        if (project.state.isEmpty()) {
                            viewer.printError("Cannot create a task because this project has no states.")
                            ViewProjectsForUserUi().show()
                        } else {
                            CreateNewTaskUi(projectId).show()
                        }
                    }

                    else -> {
                        running = false
                    }
                }
            }
        } catch (e: Exception) {
            viewer.printError("Failed to retrieve project: ${e.message}")
        }
    }
}