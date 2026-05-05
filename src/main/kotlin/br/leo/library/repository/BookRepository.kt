package br.leo.library.repository

import br.leo.library.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = LOWER(:title)")
    fun findByTitle(title: String): Optional<Book>

    @Query("SELECT b FROM Book b WHERE LOWER(b.publisher) = LOWER(:publisher) ORDER BY b.title ASC")
    fun findByPublisher(publisher: String): List<Book>
}
