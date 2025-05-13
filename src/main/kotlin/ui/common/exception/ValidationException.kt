package ui.common.exception

open class ValidationException : Exception()

class InvalidEmailFormatException : ValidationException()
class WeekPasswordException : ValidationException()
class ShortNameException : ValidationException()