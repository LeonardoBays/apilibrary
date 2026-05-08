package br.leo.library.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "authors")
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var birthDate: LocalDate,

    @Column(nullable = false, columnDefinition = "TEXT")
    var biography: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var active: Boolean = true,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val books: MutableList<Book> = mutableListOf()
) {
    constructor() : this(
        0, "", LocalDate.now(), "", LocalDateTime.now(), LocalDateTime.now(), true, mutableListOf()
    )
}

