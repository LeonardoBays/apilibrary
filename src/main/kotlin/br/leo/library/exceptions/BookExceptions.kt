package br.leo.library.exceptions

class BookNotFoundException(message: String) : RuntimeException(message)

class BookAlreadyExistsException(message: String) : RuntimeException(message)

