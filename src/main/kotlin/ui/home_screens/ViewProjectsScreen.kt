package org.example.ui.home_screen

import org.example.logic.use_case.authentication.GetCurrentLoggedInUserUseCase
import org.example.ui.Reader
import org.example.ui.UiScreen
import ui.Viewer

class ViewProjectsScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase
) : UiScreen {
    override fun show() {
        val currentUserResult = getCurrentLoggedInUserUseCase.getCurrentUser()
        
        val user = currentUserResult.getOrNull()
        if (user == null) {
            viewer.printError("No user logged in")
            return
        }
        // TODO: get user projects use case
    }
}