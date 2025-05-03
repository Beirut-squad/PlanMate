package org.example.logic.exceptions.project_magement_exceptions

class ProjectNotCreatedException (message:String) : Exception(message)
class ProjectNotEditedException (message:String) : Exception(message)
class ProjectNotDeletedException (message:String) : Exception(message)
class ProjectNotGetAllProjectsException (message:String) : Exception(message)
class BlankFieldsException(message: String) : IllegalArgumentException(message)
