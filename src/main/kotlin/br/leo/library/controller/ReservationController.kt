package br.leo.library.controller

import br.leo.library.dto.BookReservationCreateDTO
import br.leo.library.dto.BookReservationResponseDTO
import br.leo.library.dto.UserReservationsResponseDTO
import br.leo.library.service.ReservationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Book Reservations", description = "API for managing book reservations")
class ReservationController(
    private val reservationService: ReservationService
) {

    @PostMapping
    @Operation(summary = "Reserve a book", description = "Create a new book reservation for the authenticated user")
    fun reserveBook(
        @Valid @RequestBody request: BookReservationCreateDTO,
        authentication: Authentication
    ): ResponseEntity<BookReservationResponseDTO> {
        val userId = extractUserIdFromAuthentication(authentication)
        val reservation = reservationService.reserveBook(userId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation)
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "Get reservation by ID", description = "Retrieve a reservation by its ID")
    fun getReservationById(
        @PathVariable reservationId: Long
    ): ResponseEntity<BookReservationResponseDTO> {
        val reservation = reservationService.getReservationById(reservationId)
        return ResponseEntity.ok(reservation)
    }

    @PutMapping("/{reservationId}/cancel")
    @Operation(summary = "Cancel reservation", description = "Cancel an active reservation")
    fun cancelReservation(
        @PathVariable reservationId: Long,
        authentication: Authentication
    ): ResponseEntity<BookReservationResponseDTO> {
        val userId = extractUserIdFromAuthentication(authentication)
        val reservation = reservationService.cancelReservation(reservationId, userId)
        return ResponseEntity.ok(reservation)
    }

    @PutMapping("/{reservationId}/complete")
    @Operation(summary = "Complete reservation", description = "Mark a reservation as completed")
    fun completeReservation(
        @PathVariable reservationId: Long
    ): ResponseEntity<BookReservationResponseDTO> {
        val reservation = reservationService.completeReservation(reservationId)
        return ResponseEntity.ok(reservation)
    }

    @GetMapping("/user/all")
    @Operation(summary = "Get all user reservations", description = "Get all reservations for the authenticated user")
    fun getUserReservations(
        authentication: Authentication
    ): ResponseEntity<UserReservationsResponseDTO> {
        val userId = extractUserIdFromAuthentication(authentication)
        val reservations = reservationService.getUserReservations(userId)
        return ResponseEntity.ok(reservations)
    }

    @GetMapping("/user/active")
    @Operation(summary = "Get active user reservations", description = "Get all active reservations for the authenticated user")
    fun getUserActiveReservations(
        authentication: Authentication
    ): ResponseEntity<UserReservationsResponseDTO> {
        val userId = extractUserIdFromAuthentication(authentication)
        val reservations = reservationService.getUserActiveReservations(userId)
        return ResponseEntity.ok(reservations)
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get book reservation", description = "Get the active reservation for a specific book")
    fun getBookReservation(
        @PathVariable bookId: Long
    ): ResponseEntity<BookReservationResponseDTO> {
        val reservation = reservationService.getBookReservation(bookId)
        return ResponseEntity.ok(reservation)
    }

    private fun extractUserIdFromAuthentication(authentication: Authentication): Long {
        // The userId is stored in the authentication details by JwtAuthenticationFilter
        return (authentication.details as? Long) ?: throw IllegalStateException("User ID not found in authentication")
    }
}

