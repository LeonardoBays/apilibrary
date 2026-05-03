package br.leo.library.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRegistrationDTO(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    val password: String,

    @field:NotBlank(message = "Password confirmation is required")
    val passwordConfirmation: String
)

data class UserUpdateDTO(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String
)

data class UserResponseDTO(
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val active: Boolean
)

