package data.exception

open class TaskException : Exception()

class TaskCreationFailedException : TaskException()

class TaskEditFailedException : TaskException()

class TaskDeletionFailedException : TaskException()

class TaskFetchAllFailedException : TaskException()

class TaskNotFoundException : TaskException()

class FailedToReadTaskException : TaskException()
