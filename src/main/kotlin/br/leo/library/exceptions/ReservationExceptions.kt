package br.leo.library.exceptions

class ReservationAlreadyExistsException(message: String) : RuntimeException(message)
class ReservationNotFoundException(message: String) : RuntimeException(message)
class BookAlreadyReservedException(message: String) : RuntimeException(message)
class InvalidReservationStatusException(message: String) : RuntimeException(message)
