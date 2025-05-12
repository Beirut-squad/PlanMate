package ui.view.user.mate

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.components.Printer
import ui.components.Reader
import ui.components.UiScreen

class MateUi : UiScreen, KoinComponent {
    private val printer: Printer by inject()
    private val reader: Reader by inject()
    private val userProjectsUI: UserProjectsUi by inject()
    override suspend fun show() {
        printer.printTitle("Welcome to Plan Mate")

        while (true) {

            printer.printInfoLine("Choose an option:")
            printer.printOptions(
                "View Projects",
                "Log out"
            )

            val option = reader.readInt()
            when (option) {
                1 -> {
                    goToViewProjectsScreen()
                }

                2 -> {
                    printer.printGoodbyeMessage("Goodbye")
                    break
                }
            }
        }
    }

    private suspend fun goToViewProjectsScreen() {
        userProjectsUI.show()
    }
}