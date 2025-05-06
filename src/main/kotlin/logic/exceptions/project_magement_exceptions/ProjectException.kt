package org.example.logic.exceptions.project_magement_exceptions

import org.example.constants.StringConstants.Project.DUPLICATE_STATE
import org.example.constants.StringConstants.Project.NO_PROJECT_FOUND
import org.example.constants.StringConstants.Project.NO_STATE_FOUND
import org.example.constants.StringConstants.Project.STATE_HAS_TASKS


class NoProjectFoundException: Exception(NO_PROJECT_FOUND)
class DuplicateStateException: Exception(DUPLICATE_STATE)
class NoStateException: Exception(NO_STATE_FOUND)
class StateHasAssociatedTasksException: Exception(STATE_HAS_TASKS)

class ProjectNotCreatedException (message:String) : Exception(message)
class ProjectNotEditedException (message:String) : Exception(message)
class ProjectNotDeletedException (message:String) : Exception(message)
class ProjectNotGetAllProjectsException (message:String) : Exception(message)
class BlankFieldsException(message: String) : IllegalArgumentException(message)
class EmptyProjectNameException(message: String = "Should enter project name") : IllegalArgumentException(message)
class EmptyProjectDescriptionException(message: String = "Should enter project description") : IllegalArgumentException(message)