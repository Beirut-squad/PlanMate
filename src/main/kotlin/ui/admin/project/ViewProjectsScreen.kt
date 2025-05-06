package org.example.ui.admin.project

import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.models.Project
import org.example.ui.common.components.Reader
import org.example.ui.common.components.UiScreen
import org.example.ui.common.components.Viewer

class ViewProjectsScreen(
    private val viewer: Viewer,
    private val reader: Reader,
    private val getAllProjectsUseCases: GetAllProjectsUseCases,
    private val singleProjectScreen: SingleProjectScreen
) : UiScreen {
    override fun show() {

        val allProjects = getAllProjectsUseCases.getAllProjects()

        allProjects.fold(
            onSuccess = { projects ->
                if (projects.isNotEmpty()) {
                    viewer.printTitle("Project: ")
                    projects.forEachIndexed { index, project ->
                        viewer.printInfoLine(
                            """
                        ${index + 1}.
                        - Made by: ${project.creatorUserID}
                        - Name: ${project.name}
                        - Description: ${project.description}
                        - Creation Date: ${project.createdAt}
                        - Update Date: ${project.updatedAt}
                    """.trimIndent()
                        )
                    }

                    chooseProject(projects)
                } else {
                    viewer.printInfoLine("No projects found.")
                }
            },
            onFailure = {
                viewer.printError("Failed to retrieve projects: ${it.message}")
            }
        )
    }

    private fun chooseProject(projects: List<Project>) {
        viewer.printInfoLine("Choose project: ")
        viewer.printOptions(projects.map { it.name })

        enterProject(projects)
    }

    private fun enterProject(projects: List<Project>) {
        when (val input = reader.readInt()) {
            in 1..projects.size -> {
                if (input != null) {
                    goToSingleProjectScreen(projects[input - 1])
                } else {
                    viewer.printError("Invalid project number")
                    enterProject(projects)
                }
            }
            else -> {
                viewer.printError("Invalid project number")
                enterProject(projects)
            }
        }
    }

    private fun goToSingleProjectScreen(project: Project) {
        singleProjectScreen.project = project
        singleProjectScreen.show()
    }
}
