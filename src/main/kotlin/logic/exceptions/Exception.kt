package org.example.logic.exceptions

class NoProjectLogsFoundException : Exception("No project logs found")

class NoTaskLogsFoundException : Exception("No task logs found")

import kotlin.Exception


class TaskCreationException(massage: String) : Exception(massage)

class TaskEditException(massage: String) : Exception(massage)

class TaskDeletionException(massage: String) : Exception(massage)

class GetAllTasksException(massage: String) : Exception(massage)

class GetTaskException(massage: String) : Exception(massage)


class BlankFieldsException(message: String) : IllegalArgumentException(message)

class NoFieldsToUpdateException(message: String) : IllegalArgumentException(message)

