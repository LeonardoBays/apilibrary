package br.leo.library.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import java.time.LocalDateTime


@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var active: Boolean = true
) {
    constructor() : this(
        0, "", "", "", LocalDateTime.now(), LocalDateTime.now(), true
    )
}

