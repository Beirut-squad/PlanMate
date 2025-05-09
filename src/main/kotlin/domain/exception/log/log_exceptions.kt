package domain.exception.log

import org.example.ui.constants.StringConstants.Log.NO_PROJECT_LOGS_AVAILABLE
import org.example.ui.constants.StringConstants.Log.NO_TASK_LOGS_AVAILABLE

class NoProjectLogsFoundException : Exception(NO_PROJECT_LOGS_AVAILABLE)
class NoTaskLogsFoundException : Exception(NO_TASK_LOGS_AVAILABLE)