package br.leo.library.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "publishers")
class Publisher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String,

    @Column(nullable = false)
    var website: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var active: Boolean = true,

    @OneToMany(mappedBy = "publisher", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val books: MutableList<Book> = mutableListOf()
) {
    constructor() : this(
        0, "", "", "", LocalDateTime.now(), LocalDateTime.now(), true, mutableListOf()
    )
}

