package domain.exception

open class NullInputException : GeneralException()

open class EmptyFieldException : NullInputException()
