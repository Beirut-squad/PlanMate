package ui.view.project

import ui.common.exception.handler.SafeExecutor
import domain.model.Project
import domain.useCase.project.GetProjectByIdUseCase
import ui.common.exception.handler.ExceptionHandler
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import ui.view.task.CreateTaskUi
import ui.view.user.mate.UserProjectsUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.extensions.formatDateTime
import java.util.*

class ProjectMateUi(
    private val projectId: UUID,
) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val getProjectByIdUseCase: GetProjectByIdUseCase by inject()
    private val executor: SafeExecutor by inject()
    private val handler: ExceptionHandler by inject()

    var running = true

    override suspend fun show() {
        executor.tryToExecute(
            action = {
                getProjectByIdUseCase.getProjectById(projectId)
            },
            onSuccess = { project ->
                displayProjectOptions(project)
            },
            onError = {
                handler.printHandledError(it)
            }
        )
    }

    private suspend fun displayProjectOptions(project: Project) {

        while (running) {
            printer.printInfoLine(
                """
                    - Project Name : ${project.title}
                    - Description : ${project.description}
                    - Created At : ${project.createdAt.formatDateTime()}
                    """.trimIndent()
            )

            printer.printInfoLine("Choose an option :")
            printer.printOptions(
                "View state for project",
                "View all task for project",
                "Create new task",
                "Go Back"
            )

            val option = reader.readInt()
            when (option) {
                1 -> {
                    ProjectStateSelectedUi(
                        project.id,
                    ).show()
                }

                2 -> {
                    ProjectTasksUi(
                        project.id
                    ).show()
                }

                3 -> {
                    if (project.taskStates.isEmpty()) {
                        printer.printError("Can't create a task because this project has no states.")
                        UserProjectsUi().show()
                    } else {
                        CreateTaskUi(projectId).show()
                    }
                }

                4 -> {
                    running = false
                }

                else -> {
                    printer.printError("Invalid option")
                }
            }
        }
    }
}