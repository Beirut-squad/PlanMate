package data.exception

open class ProjectException : Exception()

class ProjectNotFoundException : ProjectException()

class ProjectCreationFailedException : ProjectException()

class ProjectEditFailedException : ProjectException()

class ProjectDeletionFailedException : ProjectException()

class ProjectFetchAllFailedException : ProjectException()

class DuplicateStateException : ProjectException()

class StateNotFoundException : ProjectException()
