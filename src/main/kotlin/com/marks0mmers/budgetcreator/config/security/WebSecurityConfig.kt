package com.marks0mmers.budgetcreator.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig {

    @Autowired lateinit var authenticationManager: AuthenticationManager

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(authenticationManager).apply {
            setServerAuthenticationConverter { swe ->
                swe?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION)?.let { authHeader ->
                    if (authHeader.startsWith("Bearer ")) {
                        val authToken = authHeader.substring(7)
                        return@setServerAuthenticationConverter UsernamePasswordAuthenticationToken(
                            authToken,
                            authToken
                        ).toMono()
                    }
                }
                return@setServerAuthenticationConverter Mono.empty()
            }
        }
        return http.authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers(HttpMethod.POST, "/api/users", "/api/users/login").permitAll()
            .anyExchange().authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint { swe, _ ->
                Mono.fromRunnable {
                    swe.response.statusCode = HttpStatus.UNAUTHORIZED
                }
            }
            .accessDeniedHandler { swe, _ -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN } }
            .and()
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .build()
    }
}