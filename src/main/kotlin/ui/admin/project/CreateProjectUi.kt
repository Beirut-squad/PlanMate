package org.example.ui.admin.project

import domain.use_case.project.CreateProjectUseCase
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen

class CreateProjectUi(
    private val printer: Printer,
    private val reader: Reader,
    private val createProjectUseCase: CreateProjectUseCase

) : UiScreen {
    override suspend fun show() {
        printer.printTitle("Let's create a project")
        printer.printInfoLine("Write your project name")
        val name = reader.readInput()
        printer.printOption("Tell me more about description of your project")
        val description = reader.readInput()
        try {
            createProjectUseCase.createProject(
                name = name.toString(),
                description = description.toString(),
                stateNames = emptyList(),
            )
        }catch (e:Exception){
            printer.printError("${e.message}")
        }

    }
}