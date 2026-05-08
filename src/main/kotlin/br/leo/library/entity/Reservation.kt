package br.leo.library.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reservations")
class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    val book: Book,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ReservationStatus = ReservationStatus.ACTIVE,

    @Column(name = "reserved_at", nullable = false, updatable = false)
    val reservedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "expires_at", nullable = true)
    var expiresAt: LocalDateTime? = LocalDateTime.now().plusDays(7),

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(): this(
        0, User(), Book(), ReservationStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now().plusDays(7),
        LocalDateTime.now(), LocalDateTime.now()
    )
}

enum class ReservationStatus {
    ACTIVE,
    CANCELLED,
    COMPLETED
}

