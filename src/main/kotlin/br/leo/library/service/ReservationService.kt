package br.leo.library.service

import br.leo.library.dto.BookReservationCreateDTO
import br.leo.library.dto.BookReservationResponseDTO
import br.leo.library.dto.BookReservationUpdateDTO
import br.leo.library.dto.UserReservationsResponseDTO
import br.leo.library.entity.Reservation
import br.leo.library.entity.ReservationStatus
import br.leo.library.exceptions.*
import br.leo.library.repository.BookRepository
import br.leo.library.repository.ReservationRepository
import br.leo.library.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun reserveBook(userId: Long, request: BookReservationCreateDTO): BookReservationResponseDTO {
        // Verify user exists
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User with ID $userId not found") }

        // Verify book exists
        val book = bookRepository.findById(request.bookId)
            .orElseThrow { BookNotFoundException("Book with ID ${request.bookId} not found") }

        // Check if user already has an active reservation for this book
        if (reservationRepository.existsByUserIdAndBookIdAndStatus(userId, request.bookId, ReservationStatus.ACTIVE)) {
            throw ReservationAlreadyExistsException("User already has an active reservation for this book")
        }

        // Check if book already has an active reservation
        if (reservationRepository.existsByBookIdAndStatus(request.bookId, ReservationStatus.ACTIVE)) {
            throw BookAlreadyReservedException("This book is already reserved by another user")
        }

        // Create new reservation
        val reservation = Reservation(
            user = user,
            book = book,
            status = ReservationStatus.ACTIVE,
            expiresAt = LocalDateTime.now().plusDays(7)
        )

        val savedReservation = reservationRepository.save(reservation)
        return convertToResponseDTO(savedReservation)
    }

    fun cancelReservation(reservationId: Long, userId: Long): BookReservationResponseDTO {
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { ReservationNotFoundException("Reservation with ID $reservationId not found") }

        // Verify the reservation belongs to the user
        if (reservation.user.id != userId) {
            throw ReservationNotFoundException("Reservation with ID $reservationId not found for user $userId")
        }

        // Check if reservation is already cancelled
        if (reservation.status == ReservationStatus.CANCELLED) {
            throw InvalidReservationStatusException("Reservation is already cancelled")
        }

        reservation.status = ReservationStatus.CANCELLED
        reservation.updatedAt = LocalDateTime.now()

        val updatedReservation = reservationRepository.save(reservation)
        return convertToResponseDTO(updatedReservation)
    }

    fun completeReservation(reservationId: Long): BookReservationResponseDTO {
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { ReservationNotFoundException("Reservation with ID $reservationId not found") }

        // Check if reservation is active
        if (reservation.status != ReservationStatus.ACTIVE) {
            throw InvalidReservationStatusException("Reservation status must be ACTIVE to complete")
        }

        reservation.status = ReservationStatus.COMPLETED
        reservation.updatedAt = LocalDateTime.now()

        val updatedReservation = reservationRepository.save(reservation)
        return convertToResponseDTO(updatedReservation)
    }

    fun getReservationById(reservationId: Long): BookReservationResponseDTO {
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { ReservationNotFoundException("Reservation with ID $reservationId not found") }
        return convertToResponseDTO(reservation)
    }

    fun getUserReservations(userId: Long): UserReservationsResponseDTO {
        // Verify user exists
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User with ID $userId not found") }

        val reservations = reservationRepository.findByUserId(userId)
        val reservationDTOs = reservations.map { convertToResponseDTO(it) }

        return UserReservationsResponseDTO(
            userId = user.id,
            userName = user.name,
            reservations = reservationDTOs
        )
    }

    fun getUserActiveReservations(userId: Long): UserReservationsResponseDTO {
        // Verify user exists
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User with ID $userId not found") }

        val reservations = reservationRepository.findByUserIdAndStatus(userId, ReservationStatus.ACTIVE)
        val reservationDTOs = reservations.map { convertToResponseDTO(it) }

        return UserReservationsResponseDTO(
            userId = user.id,
            userName = user.name,
            reservations = reservationDTOs
        )
    }

    fun getBookReservation(bookId: Long): BookReservationResponseDTO {
        // Verify book exists
        bookRepository.findById(bookId)
            .orElseThrow { BookNotFoundException("Book with ID $bookId not found") }

        val reservation = reservationRepository.findByBookIdAndStatus(bookId, ReservationStatus.ACTIVE)
            .orElseThrow { ReservationNotFoundException("No active reservation found for book with ID $bookId") }

        return convertToResponseDTO(reservation)
    }

    private fun convertToResponseDTO(reservation: Reservation): BookReservationResponseDTO {
        return BookReservationResponseDTO(
            id = reservation.id,
            userId = reservation.user.id,
            userName = reservation.user.name,
            bookId = reservation.book.id,
            bookTitle = reservation.book.title,
            bookAuthor = reservation.book.author,
            status = reservation.status.name,
            reservedAt = reservation.reservedAt.format(dateFormatter),
            expiresAt = reservation.expiresAt?.format(dateFormatter),
            createdAt = reservation.createdAt.format(dateFormatter),
            updatedAt = reservation.updatedAt.format(dateFormatter)
        )
    }
}
