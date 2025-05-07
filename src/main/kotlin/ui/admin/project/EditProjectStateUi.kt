import org.example.models.Project
import org.example.models.State
import org.example.ui.admin.project.CreateProjectStateUi
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditProjectStateUi(
    private val project: Project,
    private val state: State
) : UiScreen, KoinComponent {
    private val viewer: Viewer by inject()
    private val reader: Reader by inject()
    private val editStateUseCase: EditStateUseCase by inject()

    override fun show() {
            viewer.printTitle("Edit State")

            viewer.printTitle("Current state name: ${state.name}")
            viewer.printTitle("Enter new state name:")
            val newName = reader.readInput().toString()

            editStateUseCase.editState(state, newName, project).fold(
                onSuccess = {
                    viewer.printTitle("State updated successfully to: $newName")
                }, onFailure = {
                    viewer.printTitle("Error updating state: ${it.message}")
                }
            )
    }
}