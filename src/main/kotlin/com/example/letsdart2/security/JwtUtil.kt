package com.example.letsdart2.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtil(@Value("\${jwt.signing-secret}") private val secretKey: String) {

    private val jwtParser by lazy { Jwts.parser().setSigningKey(secretKey).build() }

    fun generateToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    private fun parseJwtClaims(token: String): Claims? {
        return jwtParser.parseSignedClaims(token).payload
    }

    fun resolveClaims(req: HttpServletRequest): Claims? {
        return try {
            val token = resolveToken(req)
            token?.let { parseJwtClaims(it) }
        } catch (ex: ExpiredJwtException) {
            req.setAttribute("expired", ex.message)
            throw ex
        } catch (ex: Exception) {
            req.setAttribute("invalid", ex.message)
            throw ex
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(TOKEN_HEADER)
        return if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            bearerToken.substring(TOKEN_PREFIX.length)
        } else null
    }

    fun validateClaims(claims: Claims): Boolean {
        return try {
            claims.expiration.after(Date())
        } catch (e: Exception) {
            throw e
        }
    }

    companion object {
        private const val TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 10  // 10 hours
        private const val TOKEN_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }
}