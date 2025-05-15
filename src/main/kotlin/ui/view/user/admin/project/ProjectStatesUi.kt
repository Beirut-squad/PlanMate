package ui.view.user.admin.project

import domain.model.Project
import domain.model.TaskState
import domain.useCase.project.GetProjectByIdUseCase
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen
import java.util.*

class ProjectStatesUi(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val singleStateUi: SingleStateUi,
    private val printer: Printer,
    private val reader: Reader
) : UiScreen {
    private lateinit var projectId: UUID
    private lateinit var project: Project
    private lateinit var TaskStates: List<TaskState>

    suspend fun setProject(projectId: UUID) {
        this.projectId = projectId
        this.project = getProject()
        this.TaskStates = project.taskStates
    }

    override suspend fun show() {
        while (true) {
            printer.printTitle("Project states")

            if (TaskStates.isEmpty()) {
                displayNoStatesAndGoToCreateState()
                break
            } else {
                displayAvailableStates()

                printer.printInfoLine("Choose state number or Go Back : ")
                val selectedIndex = reader.readInput().toString().toIntOrNull()?.minus(1) ?: -1

                when (selectedIndex) {
                    in TaskStates.indices -> {
                        goToSingleStateUi(TaskStates[selectedIndex])
                    }

                    TaskStates.size -> {
                        break
                    }

                    else -> {
                        printer.printError("Invalid selection")
                    }
                }
            }
            setProject(projectId)
        }
    }

    private suspend fun getProject(): Project {
        return getProjectByIdUseCase.getProjectById(projectId)
    }

    private suspend fun displayNoStatesAndGoToCreateState() {
        printer.printError("You have no states, please create one first")
        CreateProjectStateUi(project).show()
    }

    private fun displayAvailableStates() {
        printer.printCorrectOutput("Available States:")
        printer.printOptions(TaskStates.map { it.name } + "Go Back")
    }

    private suspend fun goToSingleStateUi(taskState: TaskState) {
        setProject(projectId)
        singleStateUi.setProject(project)
        singleStateUi.setState(taskState)
        singleStateUi.show()
    }
}