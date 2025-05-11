package ui.admin.home

import org.example.ui.admin.log.project.ProjectLogsUi
import org.example.ui.admin.project.CreateProjectUi
import org.example.ui.admin.project.ProjectsUi
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.admin.log.task.TaskLogsUi

class AdminUi(

) : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val projectsUi: ProjectsUi by inject()
    private val createProjectUi: CreateProjectUi by inject()
    private val projectLogs: ProjectLogsUi by inject()
    private val allTaskLogsView: TaskLogsUi by inject()

    override suspend fun show() {

        while (true) {
            printer.printInfoLine("Choose an option:")
            printer.printOptions(
                "View Current Projects",
                "Create a New Project",
                "Show all project logs",
                "Show all task logs",
                "Exit"
            )

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectScreen()
                }

                2 -> {
                    goToCreateNewProjectScreen()
                }

                3 -> {
                    goToViewAllProjectLogsScreen()
                }

                4 -> {
                    goToViewAllTaskLogsScreen()
                }

                5 -> {
                    printer.printGoodbyeMessage("Goodbye")
                    break
                }

            }
        }
    }

    private suspend fun goToViewProjectScreen() {
        projectsUi.show()
    }

    private suspend fun goToCreateNewProjectScreen() {
        createProjectUi.show()
    }

    private suspend fun goToViewAllProjectLogsScreen() {
        projectLogs.show()
    }

    private suspend fun goToViewAllTaskLogsScreen() {
        allTaskLogsView.show()
    }
}