package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.config.AuthenticationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.lang.UnsupportedOperationException

class SecurityContextRepository : ServerSecurityContextRepository {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    override fun save(swe: ServerWebExchange?, sc: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException("Not Supported Yet.")
    }

    override fun load(swe: ServerWebExchange?): Mono<SecurityContext> {
        val request = swe?.request
        val authHeader = request?.headers?.getFirst(HttpHeaders.AUTHORIZATION)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val authToken = authHeader.substring(7)
            val auth = UsernamePasswordAuthenticationToken(authToken, authToken)
            authenticationManager
                    .authenticate(auth)
                    .map(::SecurityContextImpl)
        } else Mono.empty()
    }
}