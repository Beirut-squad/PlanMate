package ui.view.user.mate

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.common.Printer
import ui.common.Reader
import ui.common.UiScreen

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
                    break
                }

                else -> {
                    printer.printError("Invalid option")
                }
            }
        }
    }

    private suspend fun goToViewProjectsScreen() {
        userProjectsUI.show()
    }
}