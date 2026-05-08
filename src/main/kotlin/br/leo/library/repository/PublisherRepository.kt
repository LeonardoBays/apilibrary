package br.leo.library.repository

import br.leo.library.entity.Publisher
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PublisherRepository : JpaRepository<Publisher, Long> {
    fun findByName(name: String): Optional<Publisher>
    fun findAllByActive(active: Boolean): List<Publisher>
}

