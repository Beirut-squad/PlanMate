package org.example.logic.exceptions.log_exceptions

import org.example.constants.StringConstants.Log.NO_PROJECT_LOGS_AVAILABLE
import org.example.constants.StringConstants.Log.NO_TASK_LOGS_AVAILABLE

class NoProjectLogsFoundException : Exception(NO_PROJECT_LOGS_AVAILABLE)
class NoTaskLogsFoundException : Exception(NO_TASK_LOGS_AVAILABLE)