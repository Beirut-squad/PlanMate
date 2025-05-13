package ui.common.exception

open class AuthenticationException : Exception()

class EmailAlreadyExistsException : AuthenticationException()

class EmailNotFoundException : AuthenticationException()

class InvalidCredentialsException : AuthenticationException()

class UsersAlreadyExistException : AuthenticationException()

class UserNotLoggedInException : AuthenticationException()

class UserNotFoundException : AuthenticationException()
