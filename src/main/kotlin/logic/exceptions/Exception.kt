package org.example.logic.exceptions

import org.example.constants.StringConstants
import org.example.models.Project


class NoProjectFoundException(project: Project) : Exception("${StringConstants.Project.NO_PROJECT_FOUND}: ${project.id}")
class CanNotAddOrEditStateException(message: String) : Exception(message)
class NoStateException(message: String) : Exception(message)
class StateHasAssociatedTasksException(message: String) : Exception(message)
