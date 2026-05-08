package br.leo.library.controller

import br.leo.library.dto.AuthorCreateDTO
import br.leo.library.dto.AuthorResponseDTO
import br.leo.library.dto.AuthorUpdateDTO
import br.leo.library.service.AuthorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Management", description = "API for managing book authors")
class AuthorController(
    private val authorService: AuthorService
) {

    @PostMapping
    @Operation(summary = "Create a new author", description = "Create a new book author with name, birth date, and biography")
    fun createAuthor(@Valid @RequestBody request: AuthorCreateDTO): ResponseEntity<AuthorResponseDTO> {
        val authorResponse = authorService.createAuthor(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(authorResponse)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieve an author by their ID")
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<AuthorResponseDTO> {
        val authorResponse = authorService.getAuthorById(id)
        return ResponseEntity.ok(authorResponse)
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieve all authors")
    fun getAllAuthors(): ResponseEntity<List<AuthorResponseDTO>> {
        val authors = authorService.getAllAuthors()
        return ResponseEntity.ok(authors)
    }

    @GetMapping("/active")
    @Operation(summary = "Get active authors", description = "Retrieve only active authors")
    fun getActiveAuthors(): ResponseEntity<List<AuthorResponseDTO>> {
        val authors = authorService.getActiveAuthors()
        return ResponseEntity.ok(authors)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update author", description = "Update author information (name, birth date, and biography)")
    fun updateAuthor(
        @PathVariable id: Long,
        @Valid @RequestBody request: AuthorUpdateDTO
    ): ResponseEntity<AuthorResponseDTO> {
        val updatedAuthor = authorService.updateAuthor(id, request)
        return ResponseEntity.ok(updatedAuthor)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete author permanently", description = "Permanently delete an author from the database")
    fun deleteAuthor(@PathVariable id: Long): ResponseEntity<Void> {
        authorService.deleteAuthor(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate author", description = "Deactivate an author (soft delete)")
    fun deactivateAuthor(@PathVariable id: Long): ResponseEntity<AuthorResponseDTO> {
        val deactivatedAuthor = authorService.deactivateAuthor(id)
        return ResponseEntity.ok(deactivatedAuthor)
    }

    @PutMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate author", description = "Reactivate a deactivated author")
    fun reactivateAuthor(@PathVariable id: Long): ResponseEntity<AuthorResponseDTO> {
        val reactivatedAuthor = authorService.reactivateAuthor(id)
        return ResponseEntity.ok(reactivatedAuthor)
    }
}

