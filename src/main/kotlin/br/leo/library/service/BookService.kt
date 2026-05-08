package br.leo.library.service

import br.leo.library.dto.BookCreateDTO
import br.leo.library.dto.BookResponseDTO
import br.leo.library.dto.BookUpdateDTO
import br.leo.library.entity.Book
import br.leo.library.exceptions.BookAlreadyExistsException
import br.leo.library.exceptions.BookNotFoundException
import br.leo.library.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class BookService(
    private val bookRepository: BookRepository
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun createBook(request: BookCreateDTO): BookResponseDTO {
        val normalizedTitle = request.title.trim()

        if (bookRepository.findByTitle(normalizedTitle).isPresent) {
            throw BookAlreadyExistsException("Book with title '$normalizedTitle' already exists")
        }

        val book = Book(
            title = normalizedTitle,
            description = request.description.trim(),
            publisher = request.publisher.trim(),
            author = request.author.trim()
        )

        val savedBook = bookRepository.save(book)
        return convertToResponseDTO(savedBook)
    }

    fun getBookById(id: Long): BookResponseDTO {
        val book = bookRepository.findById(id)
            .orElseThrow { BookNotFoundException("Book with ID $id not found") }
        return convertToResponseDTO(book)
    }

    fun getAllBooks(): List<BookResponseDTO> {
        return bookRepository.findAll().map { convertToResponseDTO(it) }
    }

    fun getBooksByPublisher(publisher: String): List<BookResponseDTO> {
        val books = bookRepository.findByPublisher(publisher.trim())
        if (books.isEmpty()) {
            throw BookNotFoundException("No books found from publisher '$publisher'")
        }
        return books.map { convertToResponseDTO(it) }
    }

    fun updateBook(id: Long, request: BookUpdateDTO): BookResponseDTO {
        val book = bookRepository.findById(id)
            .orElseThrow { BookNotFoundException("Book with ID $id not found") }

        val normalizedTitle = request.title.trim()

        // Check if another book with the same title exists
        bookRepository.findByTitle(normalizedTitle).ifPresent { existingBook ->
            if (existingBook.id != id) {
                throw BookAlreadyExistsException("Book with title '$normalizedTitle' already exists")
            }
        }

        book.title = normalizedTitle
        book.description = request.description.trim()
        book.publisher = request.publisher.trim()
        book.author = request.author.trim()
        book.updatedAt = LocalDateTime.now()

        val updatedBook = bookRepository.save(book)
        return convertToResponseDTO(updatedBook)
    }

    fun deleteBook(id: Long) {
        val book = bookRepository.findById(id)
            .orElseThrow { BookNotFoundException("Book with ID $id not found") }

        bookRepository.delete(book)
    }

    private fun convertToResponseDTO(book: Book): BookResponseDTO {
        return BookResponseDTO(
            id = book.id,
            title = book.title,
            description = book.description,
            publisher = book.publisher,
            author = book.author,
            createdAt = book.createdAt.format(dateFormatter),
            updatedAt = book.updatedAt.format(dateFormatter),
            active = book.active
        )
    }
}

