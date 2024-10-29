package com.example.letsdart2.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthorizationFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = jwtUtil.resolveToken(request)
            if (accessToken == null) {
                filterChain.doFilter(request, response)
                return
            }
            val claims = jwtUtil.resolveClaims(request)
            if (claims != null && jwtUtil.validateClaims(claims)) {
                val email = claims.subject
                val authentication = UsernamePasswordAuthenticationToken(email, null, emptyList())
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            e.printStackTrace()
        }
        filterChain.doFilter(request, response)
    }
}