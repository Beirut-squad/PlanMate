import org.example.models.Project
import org.example.ui.admin.project.CreateProjectStateUi
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditProjectStateUi(
    private val project: Project
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val editStateUseCase: EditStateUseCase by inject()

    override fun show() {
        viewer.printTitle("Edit Task State")

        val states = project.state
        if (states.isEmpty()) {
            viewer.printTitle("You have no states to edit, please create one first")
            CreateProjectStateUi(project).show()
            return
        }

        viewer.printTitle("Available States:")
        states.forEachIndexed { index, state ->
            viewer.printOptions("${index + 1}. ${state.name}")
        }

        viewer.printTitle("Select the state number you want to edit:")
        val selectedIndex = reader.readInput().toString().toIntOrNull()?.minus(1)

        if (selectedIndex == null || selectedIndex !in states.indices) {
            viewer.printTitle("Invalid selection")
            return
        }

        val selectedState = states[selectedIndex]

        viewer.printTitle("Current state name: ${selectedState.name}")
        viewer.printTitle("Enter new state name:")
        val newName = reader.readInput().toString()

        val result = editStateUseCase.editState(selectedState, newName, project)
        result.onSuccess {
            viewer.printTitle("State updated successfully to: $newName")
        }.onFailure {
            viewer.printTitle("Error updating state: ${it.message}")
        }
    }
}