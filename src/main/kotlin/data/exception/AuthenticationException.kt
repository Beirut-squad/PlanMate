package data.exception

import core.exception.AuthenticationException

class EmailAlreadyExistsException : AuthenticationException()

class InvalidEmailFormatException : AuthenticationException()

class EmailNotFoundException : AuthenticationException()

class InvalidCredentialsException : AuthenticationException()

class UsersAlreadyExistException : AuthenticationException()

class UserNotLoggedInException : AuthenticationException()
