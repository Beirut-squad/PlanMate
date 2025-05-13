package ui.common.exception

open class ProjectException : Exception()

class ProjectNotFoundException : ProjectException()

class ProjectCreationFailedException : ProjectException()

class ProjectEditFailedException : ProjectException()

class ProjectDeletionFailedException : ProjectException()

class ProjectFetchAllFailedException : ProjectException()

class DuplicateStateException : ProjectException()

class StateNotFoundException : ProjectException()

class EmptyProjectDescriptionException : EmptyFieldException()

class EmptyProjectTitleException : EmptyFieldException()

class NullProjectsComparisonException : NullInputException()

class DuplicateDescriptionException : ProjectException()

class DuplicateTitleException : ProjectException()

