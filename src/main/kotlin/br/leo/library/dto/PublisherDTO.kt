package br.leo.library.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PublisherCreateDTO(
    @field:NotBlank(message = "Publisher name cannot be blank")
    @field:Size(min = 2, max = 100, message = "Publisher name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Description cannot be blank")
    @field:Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    val description: String,

    @field:NotBlank(message = "Website cannot be blank")
    @field:Size(min = 5, max = 255, message = "Website must be between 5 and 255 characters")
    val website: String
)

data class PublisherUpdateDTO(
    @field:NotBlank(message = "Publisher name cannot be blank")
    @field:Size(min = 2, max = 100, message = "Publisher name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Description cannot be blank")
    @field:Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    val description: String,

    @field:NotBlank(message = "Website cannot be blank")
    @field:Size(min = 5, max = 255, message = "Website must be between 5 and 255 characters")
    val website: String
)

data class PublisherResponseDTO(
    val id: Long,
    val name: String,
    val description: String,
    val website: String,
    val createdAt: String,
    val updatedAt: String,
    val active: Boolean
)

