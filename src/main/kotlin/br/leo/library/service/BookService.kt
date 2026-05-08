package br.leo.library.service

import br.leo.library.dto.AuthorResponseDTO
import br.leo.library.dto.BookCreateDTO
import br.leo.library.dto.BookResponseDTO
import br.leo.library.dto.BookUpdateDTO
import br.leo.library.dto.PublisherResponseDTO
import br.leo.library.entity.Book
import br.leo.library.exceptions.AuthorNotFoundException
import br.leo.library.exceptions.BookAlreadyExistsException
import br.leo.library.exceptions.BookNotFoundException
import br.leo.library.exceptions.PublisherNotFoundException
import br.leo.library.repository.AuthorRepository
import br.leo.library.repository.BookRepository
import br.leo.library.repository.PublisherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class BookService(
    private val bookRepository: BookRepository,
    private val publisherRepository: PublisherRepository,
    private val authorRepository: AuthorRepository
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val birthDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun createBook(request: BookCreateDTO): BookResponseDTO {
        val normalizedTitle = request.title.trim()

        if (bookRepository.findByTitle(normalizedTitle).isPresent) {
            throw BookAlreadyExistsException("Book with title '$normalizedTitle' already exists")
        }

        val publisher = publisherRepository.findById(request.publisherId)
            .orElseThrow { PublisherNotFoundException("Publisher with ID ${request.publisherId} not found") }

        val author = authorRepository.findById(request.authorId)
            .orElseThrow { AuthorNotFoundException("Author with ID ${request.authorId} not found") }

        val book = Book(
            title = normalizedTitle,
            description = request.description.trim(),
            publisher = publisher,
            author = author
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

    fun getBooksByPublisher(publisherId: Long): List<BookResponseDTO> {
        val publisher = publisherRepository.findById(publisherId)
            .orElseThrow { PublisherNotFoundException("Publisher with ID $publisherId not found") }

        val books = bookRepository.findByPublisher(publisher)
        if (books.isEmpty()) {
            throw BookNotFoundException("No books found from publisher '${publisher.name}'")
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

        val publisher = publisherRepository.findById(request.publisherId)
            .orElseThrow { PublisherNotFoundException("Publisher with ID ${request.publisherId} not found") }

        val author = authorRepository.findById(request.authorId)
            .orElseThrow { AuthorNotFoundException("Author with ID ${request.authorId} not found") }

        book.title = normalizedTitle
        book.description = request.description.trim()
        book.publisher = publisher
        book.author = author
        book.updatedAt = LocalDateTime.now()

        val updatedBook = bookRepository.save(book)
        return convertToResponseDTO(updatedBook)
    }

    fun deleteBook(id: Long) {
        val book = bookRepository.findById(id)
            .orElseThrow { BookNotFoundException("Book with ID $id not found") }

        bookRepository.delete(book)
    }

    fun searchBooks(keyword: String, sortOrder: String = "asc"): List<BookResponseDTO> {
        val books = bookRepository.searchBooks(keyword)

        if (books.isEmpty()) {
            throw BookNotFoundException("No books found matching the search criteria")
        }

        val sortedBooks = when (sortOrder.lowercase()) {
            "desc" -> books.sortedByDescending { it.title }
            else -> books.sortedBy { it.title }
        }

        return sortedBooks.map { convertToResponseDTO(it) }
    }

    private fun convertToResponseDTO(book: Book): BookResponseDTO {
        val publisherDTO = PublisherResponseDTO(
            id = book.publisher.id,
            name = book.publisher.name,
            description = book.publisher.description,
            website = book.publisher.website,
            createdAt = book.publisher.createdAt.format(dateFormatter),
            updatedAt = book.publisher.updatedAt.format(dateFormatter),
            active = book.publisher.active
        )

        val authorDTO = AuthorResponseDTO(
            id = book.author.id,
            name = book.author.name,
            birthDate = book.author.birthDate.format(birthDateFormatter),
            biography = book.author.biography,
            createdAt = book.author.createdAt.format(dateFormatter),
            updatedAt = book.author.updatedAt.format(dateFormatter),
            active = book.author.active
        )

        return BookResponseDTO(
            id = book.id,
            title = book.title,
            description = book.description,
            publisher = publisherDTO,
            author = authorDTO,
            createdAt = book.createdAt.format(dateFormatter),
            updatedAt = book.updatedAt.format(dateFormatter),
            active = book.active
        )
    }
}

