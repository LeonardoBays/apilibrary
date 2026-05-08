package br.leo.library.repository

import br.leo.library.entity.Reservation
import br.leo.library.entity.ReservationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {
    // Find active reservation for a specific book
    fun findByBookIdAndStatus(bookId: Long, status: ReservationStatus): Optional<Reservation>

    // Find all active reservations for a user
    fun findByUserIdAndStatus(userId: Long, status: ReservationStatus): List<Reservation>

    // Find all reservations for a user
    fun findByUserId(userId: Long): List<Reservation>

    // Check if a book has an active reservation
    fun existsByBookIdAndStatus(bookId: Long, status: ReservationStatus): Boolean

    // Check if a user has already reserved a specific book
    fun existsByUserIdAndBookIdAndStatus(userId: Long, bookId: Long, status: ReservationStatus): Boolean
}
