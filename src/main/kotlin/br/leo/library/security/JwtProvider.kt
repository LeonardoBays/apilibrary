package br.leo.library.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.util.Base64
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret:}")
    private val secretBase64: String,
    @Value("\${jwt.expiration:86400000}")
    private val expirationMs: Long
) {

    private val key: SecretKey by lazy {
        if (secretBase64.isNotBlank()) {
            // Decode Base64 secret if provided
            val decodedKey = Base64.getDecoder().decode(secretBase64)
            Keys.hmacShaKeyFor(decodedKey)
        } else {
            // Generate a secure key if no secret is provided
            Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512)
        }
    }

    fun generateToken(email: String, userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        return Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun getEmailFromToken(token: String): String {
        return getClaimsFromToken(token).subject
    }

    fun getUserIdFromToken(token: String): Long {
        val claim = getClaimsFromToken(token).get("userId")
        return when (claim) {
            is Long -> claim
            is Int -> claim.toLong()
            is Number -> claim.toLong()
            else -> throw IllegalArgumentException("userId claim is not a valid number")
        }
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            true
        } catch (ex: ExpiredJwtException) {
            false
        } catch (@Suppress("UNUSED_PARAMETER") ex: Exception) {
            false
        }
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}





