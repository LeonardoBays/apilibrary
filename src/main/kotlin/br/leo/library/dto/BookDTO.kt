package br.leo.library.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class BookCreateDTO(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    val title: String,

    @field:NotBlank(message = "Description is required")
    @field:Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    val description: String,

    val publisherId: Long,

    val authorId: Long
)

data class BookUpdateDTO(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    val title: String,

    @field:NotBlank(message = "Description is required")
    @field:Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    val description: String,

    val publisherId: Long,

    val authorId: Long
)

data class BookResponseDTO(
    val id: Long,
    val title: String,
    val description: String,
    val publisher: PublisherResponseDTO,
    val author: AuthorResponseDTO,
    val createdAt: String,
    val updatedAt: String,
    val active: Boolean
)

