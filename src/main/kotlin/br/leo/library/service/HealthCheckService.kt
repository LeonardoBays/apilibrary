package br.leo.library.service

import br.leo.library.controller.HealthStatus
import br.leo.library.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class HealthCheckService(
    private val userRepository: UserRepository
) {

    fun checkHealth(): HealthStatus {
        val databaseStatus = try {
            checkDatabaseConnection()
            "Connected"
        } catch (e: Exception) {
            "Disconnected"
        }

        return HealthStatus(
            timestamp = LocalDateTime.now(),
            database = databaseStatus,
        )
    }

    fun checkDatabaseConnection(): Boolean {
        return try {
            userRepository.count()
            true
        } catch (e: Exception) {
            false
        }
    }
}

