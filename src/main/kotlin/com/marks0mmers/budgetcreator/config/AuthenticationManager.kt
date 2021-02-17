package com.marks0mmers.budgetcreator.config

import com.marks0mmers.budgetcreator.models.persistent.Role
import com.marks0mmers.budgetcreator.util.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import reactor.core.publisher.Mono

@Component
class AuthenticationManager : ReactiveAuthenticationManager {
    @Autowired lateinit var jwtUtil: JWTUtil

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val authToken = authentication?.credentials.toString()
        val username = jwtUtil.getUsernameFromToken(authToken) ?: return Mono.empty()
        return if (jwtUtil.validateToken(authToken)) {
            val claims = jwtUtil.getAllClaimsFromToken(authToken)
            val rolesList = claims?.get("role", List::class.java)
            val roles = rolesList?.map { Role.valueOf(it.toString()) }
            Mono.just(
                UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles?.map { SimpleGrantedAuthority(it.name) }
                )
            )
        } else {
            Mono.empty()
        }
    }
}