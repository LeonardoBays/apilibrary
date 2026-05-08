package br.leo.library.controller

import br.leo.library.dto.BookCreateDTO
import br.leo.library.dto.BookResponseDTO
import br.leo.library.dto.BookUpdateDTO
import br.leo.library.service.BookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "API for managing books")
class BookController(
    private val bookService: BookService
) {

    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book with title, description, and publisher")
    fun createBook(@Valid @RequestBody request: BookCreateDTO): ResponseEntity<BookResponseDTO> {
        val bookResponse = bookService.createBook(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse)
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve all books from the library")
    fun getAllBooks(): ResponseEntity<List<BookResponseDTO>> {
        val books = bookService.getAllBooks()
        return ResponseEntity.ok(books)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a book by its ID")
    fun getBookById(@PathVariable id: Long): ResponseEntity<BookResponseDTO> {
        val bookResponse = bookService.getBookById(id)
        return ResponseEntity.ok(bookResponse)
    }

    @GetMapping("/publisher/{publisherId}")
    @Operation(summary = "Get books by publisher", description = "Retrieve all books from a specific publisher by publisher ID")
    fun getBooksByPublisher(@PathVariable publisherId: Long): ResponseEntity<List<BookResponseDTO>> {
        val books = bookService.getBooksByPublisher(publisherId)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search for books by title, description, author name, or publisher name. Sort results in ascending or descending order.")
    fun searchBooks(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "asc") sortOrder: String
    ): ResponseEntity<List<BookResponseDTO>> {
        val books = bookService.searchBooks(keyword, sortOrder)
        return ResponseEntity.ok(books)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update book information (title, description, and publisher)")
    fun updateBook(
        @PathVariable id: Long,
        @Valid @RequestBody request: BookUpdateDTO
    ): ResponseEntity<BookResponseDTO> {
        val updatedBook = bookService.updateBook(id, request)
        return ResponseEntity.ok(updatedBook)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book permanently", description = "Permanently delete a book from the library")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<Void> {
        bookService.deleteBook(id)
        return ResponseEntity.noContent().build()
    }
}

