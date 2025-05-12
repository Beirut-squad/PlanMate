package domain.exception

class EmptyTaskTitleException : EmptyFieldException()

class EmptyTaskDescriptionException : EmptyFieldException()

class NullTasksComparisonException: NullInputException()
