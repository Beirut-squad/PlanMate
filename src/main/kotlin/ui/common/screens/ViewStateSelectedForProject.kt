package org.example.ui.common.screens

import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.models.State
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer
import java.util.*

class ViewStateSelectedForProject(
    private val viewer: Viewer,
    private val state:State,
    private val projectId: UUID,
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId
):UiScreen {
    override fun show() {

        val result = getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, state.id)
        viewer.printTitle("State Details")
        viewer.printInfoLine("Name: ${state.name}")
        viewer.printInfoLine("State: $result")
    }
}