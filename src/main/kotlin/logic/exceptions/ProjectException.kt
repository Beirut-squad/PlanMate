package org.example.logic.exceptions

import org.example.constants.StringConstants.Project.DUPLICATE_STATE
import org.example.constants.StringConstants.Project.NO_PROJECT_FOUND
import org.example.constants.StringConstants.Project.NO_STATE_FOUND
import org.example.constants.StringConstants.Project.STATE_HAS_TASKS


class NoProjectFoundException: Exception(NO_PROJECT_FOUND)
class DuplicateStateException: Exception(DUPLICATE_STATE)
class NoStateException: Exception(NO_STATE_FOUND)
class StateHasAssociatedTasksException: Exception(STATE_HAS_TASKS)
