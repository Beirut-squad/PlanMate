package org.example.ui.admin.project

import org.example.models.Project
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class SingleProjectScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val deleteProjectScreen: DeleteProjectScreen
): UiScreen {
    var project: Project? = null

    override fun show() {
        viewer.printTitle("Project ${project?.name}")
        viewer.printInfoLine("What would you like to do?")

        viewer.printOptions(
            "Edit project",
            "Delete project",
            "View project states"
        )

        takeUserInput()
    }

    private fun takeUserInput() {
        val input = reader.readInt()
        when (input) {
            1 -> {
                // TODO
            }
            2 -> {
                deleteProjectScreen.show()
            }
            3 -> {
                // TODO
            }
            else -> {
                viewer.printError("Invalid option")
                takeUserInput()
            }
        }
    }
}