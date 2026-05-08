package br.leo.library.service

import br.leo.library.dto.PublisherCreateDTO
import br.leo.library.dto.PublisherResponseDTO
import br.leo.library.dto.PublisherUpdateDTO
import br.leo.library.entity.Publisher
import br.leo.library.exceptions.PublisherAlreadyExistsException
import br.leo.library.exceptions.PublisherNotFoundException
import br.leo.library.repository.PublisherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class PublisherService(
    private val publisherRepository: PublisherRepository
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun createPublisher(request: PublisherCreateDTO): PublisherResponseDTO {
        val normalizedName = request.name.trim()

        if (publisherRepository.findByName(normalizedName).isPresent) {
            throw PublisherAlreadyExistsException("Publisher with name '$normalizedName' already exists")
        }

        val publisher = Publisher(
            name = normalizedName,
            description = request.description.trim(),
            website = request.website.trim()
        )

        val savedPublisher = publisherRepository.save(publisher)
        return convertToResponseDTO(savedPublisher)
    }

    fun getPublisherById(id: Long): PublisherResponseDTO {
        val publisher = publisherRepository.findById(id)
            .orElseThrow { PublisherNotFoundException("Publisher with ID $id not found") }
        return convertToResponseDTO(publisher)
    }

    fun getAllPublishers(): List<PublisherResponseDTO> {
        return publisherRepository.findAll().map { convertToResponseDTO(it) }
    }

    fun getActivePublishers(): List<PublisherResponseDTO> {
        return publisherRepository.findAllByActive(true).map { convertToResponseDTO(it) }
    }

    fun updatePublisher(id: Long, request: PublisherUpdateDTO): PublisherResponseDTO {
        val publisher = publisherRepository.findById(id)
            .orElseThrow { PublisherNotFoundException("Publisher with ID $id not found") }

        val normalizedName = request.name.trim()

        // Check if another publisher with the same name exists
        publisherRepository.findByName(normalizedName).ifPresent { existingPublisher ->
            if (existingPublisher.id != id) {
                throw PublisherAlreadyExistsException("Publisher with name '$normalizedName' already exists")
            }
        }

        publisher.name = normalizedName
        publisher.description = request.description.trim()
        publisher.website = request.website.trim()
        publisher.updatedAt = LocalDateTime.now()

        val updatedPublisher = publisherRepository.save(publisher)
        return convertToResponseDTO(updatedPublisher)
    }

    fun deletePublisher(id: Long) {
        val publisher = publisherRepository.findById(id)
            .orElseThrow { PublisherNotFoundException("Publisher with ID $id not found") }

        publisherRepository.delete(publisher)
    }

    fun deactivatePublisher(id: Long): PublisherResponseDTO {
        val publisher = publisherRepository.findById(id)
            .orElseThrow { PublisherNotFoundException("Publisher with ID $id not found") }

        publisher.active = false
        publisher.updatedAt = LocalDateTime.now()

        val updatedPublisher = publisherRepository.save(publisher)
        return convertToResponseDTO(updatedPublisher)
    }

    fun reactivatePublisher(id: Long): PublisherResponseDTO {
        val publisher = publisherRepository.findById(id)
            .orElseThrow { PublisherNotFoundException("Publisher with ID $id not found") }

        publisher.active = true
        publisher.updatedAt = LocalDateTime.now()

        val updatedPublisher = publisherRepository.save(publisher)
        return convertToResponseDTO(updatedPublisher)
    }

    private fun convertToResponseDTO(publisher: Publisher): PublisherResponseDTO {
        return PublisherResponseDTO(
            id = publisher.id,
            name = publisher.name,
            description = publisher.description,
            website = publisher.website,
            createdAt = publisher.createdAt.format(dateFormatter),
            updatedAt = publisher.updatedAt.format(dateFormatter),
            active = publisher.active
        )
    }
}

