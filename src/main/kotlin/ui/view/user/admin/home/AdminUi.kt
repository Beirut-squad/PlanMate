package ui.view.user.admin.home

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import ui.view.user.admin.log.project.ProjectLogsUi
import ui.view.user.admin.log.task.TaskLogsUi
import ui.view.user.admin.project.CreateProjectUi
import ui.view.user.admin.project.ProjectsUi

class AdminUi : UiScreen, KoinComponent {
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
                "Log out"
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