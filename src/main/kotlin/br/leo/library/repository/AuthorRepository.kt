package br.leo.library.repository

import br.leo.library.entity.Author
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AuthorRepository : JpaRepository<Author, Long> {
    fun findByName(name: String): Optional<Author>
    fun findAllByActive(active: Boolean): List<Author>
}

