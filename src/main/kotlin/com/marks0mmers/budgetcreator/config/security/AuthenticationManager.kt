package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.models.constants.Role
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import org.springframework.http.HttpStatus
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
        return authentication?.credentials.toString().let { authToken ->
            if (jwtUtil.validateToken(authToken)) {
                val username = jwtUtil.getUsernameFromToken(authToken) ?: return Mono.empty()
                val roles = jwtUtil
                    .getAllClaimsFromToken(authToken)
                    ?.get("role", List::class.java)
                    ?.map { Role.valueOf(it.toString()) }
                    ?.map { SimpleGrantedAuthority(it.name) }
                UsernamePasswordAuthenticationToken(username, null, roles).toMono()
            } else null
        } ?: Mono.error(BudgetCreatorException("JWT Token Invalid", HttpStatus.UNAUTHORIZED))
    }
}