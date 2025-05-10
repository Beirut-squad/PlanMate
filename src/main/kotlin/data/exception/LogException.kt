package data.exception

open class LogException : Exception()

class NoProjectLogsFoundException : LogException()

class NoTaskLogsFoundException : LogException()

