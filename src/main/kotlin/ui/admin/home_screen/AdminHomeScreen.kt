package org.example.ui.admin.home_screen

import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.admin.project.CreateNewProjectScreen
import org.example.ui.common.screens.ViewProjectsScreen
import org.example.ui.common.components.Viewer

class AdminHomeScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val viewProjectsScreen: ViewProjectsScreen,
    private val createNewProjectScreen: CreateNewProjectScreen,
) : UiScreen {
    override fun show() {

        var running = true
        while (running) {

            viewer.printInfoLine("Choose an option:")
            viewer.printOptions("View Current Projects", "Create a New Project", "Exit")

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectScreen()
                    running = false
                }

                2 -> {
                    goToCreateNewProjectScreen()
                    running = false
                }

                3 -> {
                    viewer.printGoodbyeMessage("Goodbye")
                    break
                }

            }
        }
    }

    private fun goToViewProjectScreen() {
        viewProjectsScreen.show()
    }

    private fun goToCreateNewProjectScreen() {
        createNewProjectScreen.show()
    }
}