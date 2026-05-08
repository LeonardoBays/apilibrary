package br.leo.library.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class BookReservationCreateDTO(
    @field:NotNull(message = "Book ID is required")
    val bookId: Long
)

data class BookReservationUpdateDTO(
    @field:NotNull(message = "Status is required")
    val status: String
)

data class BookReservationResponseDTO(
    val id: Long,
    val userId: Long,
    val userName: String,
    val bookId: Long,
    val bookTitle: String,
    val bookAuthor: String,
    val status: String,
    val reservedAt: String,
    val expiresAt: String?,
    val createdAt: String,
    val updatedAt: String
)

data class UserReservationsResponseDTO(
    val userId: Long,
    val userName: String,
    val reservations: List<BookReservationResponseDTO>
)
