package br.leo.library.controller

import br.leo.library.dto.LoginRequestDTO
import br.leo.library.dto.LoginResponseDTO
import br.leo.library.dto.UserRegistrationDTO
import br.leo.library.dto.UserResponseDTO
import br.leo.library.dto.UserUpdateDTO
import br.leo.library.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API for managing users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and get JWT token")
    fun loginUser(@Valid @RequestBody request: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val loginResponse = userService.loginUser(request)
        return ResponseEntity.ok(loginResponse)
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user with email and password")
    fun registerUser(@Valid @RequestBody request: UserRegistrationDTO): ResponseEntity<UserResponseDTO> {
        val userResponse = userService.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponseDTO> {
        val userResponse = userService.getUserById(id)
        return ResponseEntity.ok(userResponse)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user information (name and email)")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateDTO
    ): ResponseEntity<UserResponseDTO> {
        val updatedUser = userService.updateUser(id, request)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user permanently", description = "Permanently delete a user from the database")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}

