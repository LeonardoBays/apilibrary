package br.leo.library.controller

import br.leo.library.dto.PublisherCreateDTO
import br.leo.library.dto.PublisherResponseDTO
import br.leo.library.dto.PublisherUpdateDTO
import br.leo.library.service.PublisherService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/publishers")
@Tag(name = "Publisher Management", description = "API for managing book publishers")
class PublisherController(
    private val publisherService: PublisherService
) {

    @PostMapping
    @Operation(summary = "Create a new publisher", description = "Create a new book publisher")
    fun createPublisher(@Valid @RequestBody request: PublisherCreateDTO): ResponseEntity<PublisherResponseDTO> {
        val publisherResponse = publisherService.createPublisher(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherResponse)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get publisher by ID", description = "Retrieve a publisher by their ID")
    fun getPublisherById(@PathVariable id: Long): ResponseEntity<PublisherResponseDTO> {
        val publisherResponse = publisherService.getPublisherById(id)
        return ResponseEntity.ok(publisherResponse)
    }

    @GetMapping
    @Operation(summary = "Get all publishers", description = "Retrieve all publishers")
    fun getAllPublishers(): ResponseEntity<List<PublisherResponseDTO>> {
        val publishers = publisherService.getAllPublishers()
        return ResponseEntity.ok(publishers)
    }

    @GetMapping("/active")
    @Operation(summary = "Get active publishers", description = "Retrieve only active publishers")
    fun getActivePublishers(): ResponseEntity<List<PublisherResponseDTO>> {
        val publishers = publisherService.getActivePublishers()
        return ResponseEntity.ok(publishers)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update publisher", description = "Update publisher information (name, description, website)")
    fun updatePublisher(
        @PathVariable id: Long,
        @Valid @RequestBody request: PublisherUpdateDTO
    ): ResponseEntity<PublisherResponseDTO> {
        val updatedPublisher = publisherService.updatePublisher(id, request)
        return ResponseEntity.ok(updatedPublisher)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete publisher permanently", description = "Permanently delete a publisher from the database")
    fun deletePublisher(@PathVariable id: Long): ResponseEntity<Void> {
        publisherService.deletePublisher(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate publisher", description = "Deactivate a publisher (soft delete)")
    fun deactivatePublisher(@PathVariable id: Long): ResponseEntity<PublisherResponseDTO> {
        val deactivatedPublisher = publisherService.deactivatePublisher(id)
        return ResponseEntity.ok(deactivatedPublisher)
    }

    @PutMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate publisher", description = "Reactivate a deactivated publisher")
    fun reactivatePublisher(@PathVariable id: Long): ResponseEntity<PublisherResponseDTO> {
        val reactivatedPublisher = publisherService.reactivatePublisher(id)
        return ResponseEntity.ok(reactivatedPublisher)
    }
}

