package br.leo.library.exceptions

class AuthorAlreadyExistsException(message: String) : RuntimeException(message)

class AuthorNotFoundException(message: String) : RuntimeException(message)

