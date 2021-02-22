package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.models.constants.Role
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class AuthenticationManager(private val jwtUtil: JWTUtil) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val authToken = authentication?.credentials.toString()
        val username = jwtUtil.getUsernameFromToken(authToken) ?: return Mono.empty()
        return if (jwtUtil.validateToken(authToken)) {
            val claims = jwtUtil.getAllClaimsFromToken(authToken)
            val rolesList = claims?.get("role", List::class.java)
            val roles = rolesList?.map { Role.valueOf(it.toString()) }
            UsernamePasswordAuthenticationToken(
                username,
                null,
                roles?.map { SimpleGrantedAuthority(it.name) }
            ).toMono()
        } else {
            Mono.empty()
        }
    }
}