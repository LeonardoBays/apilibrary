package br.leo.library.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequestDTO(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

data class LoginResponseDTO(
    val token: String,
    val userId: Long,
    val email: String,
    val name: String
)

