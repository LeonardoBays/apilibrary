package br.leo.library.service

import br.leo.library.dto.AuthorCreateDTO
import br.leo.library.dto.AuthorResponseDTO
import br.leo.library.dto.AuthorUpdateDTO
import br.leo.library.entity.Author
import br.leo.library.exceptions.AuthorAlreadyExistsException
import br.leo.library.exceptions.AuthorNotFoundException
import br.leo.library.repository.AuthorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class AuthorService(
    private val authorRepository: AuthorRepository
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val birthDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun createAuthor(request: AuthorCreateDTO): AuthorResponseDTO {
        val normalizedName = request.name.trim()

        if (authorRepository.findByName(normalizedName).isPresent) {
            throw AuthorAlreadyExistsException("Author with name '$normalizedName' already exists")
        }

        val author = Author(
            name = normalizedName,
            birthDate = request.birthDate,
            biography = request.biography.trim()
        )

        val savedAuthor = authorRepository.save(author)
        return convertToResponseDTO(savedAuthor)
    }

    fun getAuthorById(id: Long): AuthorResponseDTO {
        val author = authorRepository.findById(id)
            .orElseThrow { AuthorNotFoundException("Author with ID $id not found") }
        return convertToResponseDTO(author)
    }

    fun getAllAuthors(): List<AuthorResponseDTO> {
        return authorRepository.findAll().map { convertToResponseDTO(it) }
    }

    fun getActiveAuthors(): List<AuthorResponseDTO> {
        return authorRepository.findAllByActive(true).map { convertToResponseDTO(it) }
    }

    fun updateAuthor(id: Long, request: AuthorUpdateDTO): AuthorResponseDTO {
        val author = authorRepository.findById(id)
            .orElseThrow { AuthorNotFoundException("Author with ID $id not found") }

        val normalizedName = request.name.trim()

        // Check if another author with the same name exists
        authorRepository.findByName(normalizedName).ifPresent { existingAuthor ->
            if (existingAuthor.id != id) {
                throw AuthorAlreadyExistsException("Author with name '$normalizedName' already exists")
            }
        }

        author.name = normalizedName
        author.birthDate = request.birthDate
        author.biography = request.biography.trim()
        author.updatedAt = LocalDateTime.now()

        val updatedAuthor = authorRepository.save(author)
        return convertToResponseDTO(updatedAuthor)
    }

    fun deleteAuthor(id: Long) {
        val author = authorRepository.findById(id)
            .orElseThrow { AuthorNotFoundException("Author with ID $id not found") }

        authorRepository.delete(author)
    }

    fun deactivateAuthor(id: Long): AuthorResponseDTO {
        val author = authorRepository.findById(id)
            .orElseThrow { AuthorNotFoundException("Author with ID $id not found") }

        author.active = false
        author.updatedAt = LocalDateTime.now()

        val updatedAuthor = authorRepository.save(author)
        return convertToResponseDTO(updatedAuthor)
    }

    fun reactivateAuthor(id: Long): AuthorResponseDTO {
        val author = authorRepository.findById(id)
            .orElseThrow { AuthorNotFoundException("Author with ID $id not found") }

        author.active = true
        author.updatedAt = LocalDateTime.now()

        val updatedAuthor = authorRepository.save(author)
        return convertToResponseDTO(updatedAuthor)
    }

    private fun convertToResponseDTO(author: Author): AuthorResponseDTO {
        return AuthorResponseDTO(
            id = author.id,
            name = author.name,
            birthDate = author.birthDate.format(birthDateFormatter),
            biography = author.biography,
            createdAt = author.createdAt.format(dateFormatter),
            updatedAt = author.updatedAt.format(dateFormatter),
            active = author.active
        )
    }
}

