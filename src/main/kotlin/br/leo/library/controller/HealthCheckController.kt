package br.leo.library.controller

import br.leo.library.service.HealthCheckService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "API health and database connectivity status")
class HealthCheckController(
    private val healthCheckService: HealthCheckService
) {

    @GetMapping
    @Operation(summary = "Health check", description = "Check if API is running and database is connected")
    fun healthCheck(): ResponseEntity<HealthStatus> {
        val status = healthCheckService.checkHealth()
        return ResponseEntity.ok(status)
    }
}

data class HealthStatus(
    val timestamp: LocalDateTime,
    val database: String,
)

