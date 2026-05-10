package br.leo.library.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class HttpLoggingFilter : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(HttpLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val startTime = System.currentTimeMillis()

        try {
            filterChain.doFilter(request, response)
        } finally {
            val executionTime = System.currentTimeMillis() - startTime
            val authentication: Authentication? = SecurityContextHolder.getContext().authentication

            // Log only authenticated requests
            if (authentication != null && authentication.isAuthenticated &&
                authentication.principal != "anonymousUser") {

                val user = authentication.principal as String
                val userId = authentication.details as? Long ?: "N/A"
                val statusCode = response.status

                val statusEmoji = when {
                    statusCode in 200..299 -> "✓"
                    statusCode in 300..399 -> "→"
                    statusCode in 400..499 -> "✗"
                    else -> "⚠"
                }

                logger.info("$statusEmoji Response completed | User: {} | User ID: {} | Method: {} | Path: {} | Status: {} | Time: {}ms",
                    user, userId, request.method, request.requestURI, statusCode, executionTime)
            }
        }
    }
}

