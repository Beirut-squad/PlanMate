package domain.exception

open class AuthenticationException : Exception()

class EmailAlreadyExistsException : AuthenticationException()

class InvalidEmailFormatException : AuthenticationException()

class EmailNotFoundException : AuthenticationException()

class InvalidCredentialsException : AuthenticationException()

class UsersAlreadyExistException : AuthenticationException()

class UserNotLoggedInException : AuthenticationException()

class UserNotFoundException : AuthenticationException()
