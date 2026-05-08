package br.leo.library.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class AuthorCreateDTO(
    @field:NotBlank(message = "Author name cannot be blank")
    @field:Size(min = 3, max = 100, message = "Author name must be between 3 and 100 characters")
    val name: String,

    @field:NotNull(message = "Birth date cannot be null")
    val birthDate: LocalDate,

    @field:NotBlank(message = "Biography cannot be blank")
    @field:Size(min = 20, max = 2000, message = "Biography must be between 20 and 2000 characters")
    val biography: String
)

data class AuthorUpdateDTO(
    @field:NotBlank(message = "Author name cannot be blank")
    @field:Size(min = 3, max = 100, message = "Author name must be between 3 and 100 characters")
    val name: String,

    @field:NotNull(message = "Birth date cannot be null")
    val birthDate: LocalDate,

    @field:NotBlank(message = "Biography cannot be blank")
    @field:Size(min = 20, max = 2000, message = "Biography must be between 20 and 2000 characters")
    val biography: String
)

data class AuthorResponseDTO(
    val id: Long,
    val name: String,
    val birthDate: String,
    val biography: String,
    val createdAt: String,
    val updatedAt: String,
    val active: Boolean
)

