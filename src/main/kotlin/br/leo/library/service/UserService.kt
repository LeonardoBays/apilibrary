package br.leo.library.service

import br.leo.library.dto.UserRegistrationDTO
import br.leo.library.dto.UserResponseDTO
import br.leo.library.dto.UserUpdateDTO
import br.leo.library.entity.User
import br.leo.library.exceptions.EmailAlreadyExistsException
import br.leo.library.exceptions.InvalidPasswordException
import br.leo.library.exceptions.PasswordMismatchException
import br.leo.library.exceptions.UserNotFoundException
import br.leo.library.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun registerUser(request: UserRegistrationDTO): UserResponseDTO {
        if (request.password != request.passwordConfirmation) {
            throw PasswordMismatchException("Password and password confirmation do not match")
        }

        validatePasswordStrength(request.password)

        val normalizedEmail = request.email.lowercase().trim()

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw EmailAlreadyExistsException("Email $normalizedEmail is already registered")
        }

        val encodedPassword = requireNotNull(passwordEncoder.encode(request.password)) {
            "Failed to encode password"
        }

        val user = User(
            name = request.name.trim(),
            email = normalizedEmail,
            password = encodedPassword
        )

        val savedUser = userRepository.save(user)
        return convertToResponseDTO(savedUser)
    }

    fun getUserById(id: Long): UserResponseDTO {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with ID $id not found") }
        return convertToResponseDTO(user)
    }

    fun updateUser(id: Long, request: UserUpdateDTO): UserResponseDTO {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with ID $id not found") }

        val normalizedEmail = request.email.lowercase().trim()

        if (user.email != normalizedEmail && userRepository.existsByEmail(normalizedEmail)) {
            throw EmailAlreadyExistsException("Email $normalizedEmail is already registered")
        }

        user.name = request.name.trim()
        user.email = normalizedEmail
        user.updatedAt = LocalDateTime.now()

        val updatedUser = userRepository.save(user)
        return convertToResponseDTO(updatedUser)
    }

    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with ID $id not found") }

        userRepository.delete(user)
    }

    private fun convertToResponseDTO(user: User): UserResponseDTO {
        return UserResponseDTO(
            id = user.id,
            name = user.name,
            email = user.email,
            createdAt = user.createdAt.format(dateFormatter),
            updatedAt = user.updatedAt.format(dateFormatter),
            active = user.active
        )
    }

    private fun validatePasswordStrength(password: String) {
        when {
            password.length < 6 -> throw InvalidPasswordException("Password must be at least 6 characters long")
            password.length > 255 -> throw InvalidPasswordException("Password must not exceed 255 characters")
            !password.any { it.isUpperCase() } -> throw InvalidPasswordException("Password must contain at least one uppercase letter")
            !password.any { it.isLowerCase() } -> throw InvalidPasswordException("Password must contain at least one lowercase letter")
            !password.any { it.isDigit() } -> throw InvalidPasswordException("Password must contain at least one digit")
            !password.any { it in "@\$!%*?&" } -> throw InvalidPasswordException("Password must contain at least one special character (@\$!%*?&)")
        }
    }
}

