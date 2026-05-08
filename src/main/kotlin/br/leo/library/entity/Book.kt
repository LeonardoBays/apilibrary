package br.leo.library.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "books")
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String,

    @Column(nullable = false)
    var publisher: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT ''")
    var author: String = "",

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var active: Boolean = true
) {
    constructor() : this(
        0, "", "", "", "", LocalDateTime.now(), LocalDateTime.now(), true
    )
}

