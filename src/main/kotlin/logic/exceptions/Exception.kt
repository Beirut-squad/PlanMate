package org.example.logic.exceptions

import org.example.constants.StringConstants

class NoProjectLogsFoundException : Exception("No project logs found")

class NoTaskLogsFoundException : Exception("No task logs found")

class MaxRetriesExceededException(message: String = "Maximum input retries exceeded.") : IllegalStateException(message)
class NullInputException(message: String = StringConstants.General.INVALID_INPUT) : IllegalStateException(message)
