package org.example.ui.admin.project

import org.example.models.Project
import org.example.models.State
import org.example.ui.common.components.UiScreen

class SingleStateUi : UiScreen {
    private lateinit var project: Project
    private lateinit var state: State

    fun setProject(project: Project) {
        this.project = project
    }

    fun setState(state: State) {
        this.state = state
    }

    override fun show() {
        TODO("Not yet implemented")
    }
}