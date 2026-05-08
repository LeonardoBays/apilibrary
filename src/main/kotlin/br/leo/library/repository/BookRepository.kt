package br.leo.library.repository

import br.leo.library.entity.Book
import br.leo.library.entity.Publisher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = LOWER(:title)")
    fun findByTitle(title: String): Optional<Book>

    @Query("SELECT b FROM Book b WHERE b.publisher = :publisher ORDER BY b.title ASC")
    fun findByPublisher(publisher: Publisher): List<Book>

    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.publisher.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    fun searchBooks(keyword: String): List<Book>
}
