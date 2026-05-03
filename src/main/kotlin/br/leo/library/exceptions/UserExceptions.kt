package br.leo.library.exceptions

class UserNotFoundException(message: String) : RuntimeException(message)

class EmailAlreadyExistsException(message: String) : RuntimeException(message)

class PasswordMismatchException(message: String) : RuntimeException(message)

class InvalidPasswordException(message: String) : RuntimeException(message)

