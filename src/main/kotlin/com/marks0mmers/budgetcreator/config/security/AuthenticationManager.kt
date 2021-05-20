package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.models.persistent.Role
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * The manager that takes a JWT token and gets the username and roles from it
 *
 * @see ReactiveAuthenticationManager
 * @author Mark Sommers
 *
 * @constructor
 * Builds the Authentication manager with the needed dependencies
 *
 * @param jwtUtil The [JWTUtil] object needed
 */
class AuthenticationManager(
    jwtUtil: JWTUtil
) : ReactiveAuthenticationManager by ReactiveAuthenticationManager({ authentication ->
    authentication?.credentials.toString().let { authToken ->
        if (jwtUtil.validateToken(authToken)) {
            val username = jwtUtil.getAllClaimsFromToken(authToken)?.subject
                ?: return@ReactiveAuthenticationManager Mono.empty()
            val roles = jwtUtil
                .getAllClaimsFromToken(authToken)
                ?.get("role", List::class.java)
                ?.map { Role.valueOf(it.toString()) }
            UsernamePasswordAuthenticationToken(username, authentication.credentials, roles).toMono()
        } else return@ReactiveAuthenticationManager Mono.empty()
    }
})
