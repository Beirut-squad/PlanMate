package logic.exceptions.task_management_exception

class TaskCreationException(massage: String) : Exception(massage)

class TaskEditException(massage: String) : Exception(massage)

class TaskDeletionException(massage: String) : Exception(massage)

class GetAllTasksException(massage: String) : Exception(massage)

class GetTaskException(massage: String) : Exception(massage)

class BlankFieldsException(message: String) : IllegalArgumentException(message)

class NoFieldsToUpdateException(message: String) : IllegalArgumentException(message)

class FailedToReadTaskException(message: String) : IllegalArgumentException(message)