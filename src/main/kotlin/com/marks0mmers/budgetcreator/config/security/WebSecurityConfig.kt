package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.util.corsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.fromRunnable
import reactor.kotlin.core.publisher.toMono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(val authenticationManager: AuthenticationManager) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(authenticationManager).apply {
            setServerAuthenticationConverter { swe ->
                val authHeader = swe?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION) ?: ""
                if (authHeader.startsWith("Bearer ")) {
                    val authToken = authHeader.substring(7)
                    return@setServerAuthenticationConverter UsernamePasswordAuthenticationToken(
                        authToken,
                        authToken
                    ).toMono()
                }
                return@setServerAuthenticationConverter Mono.empty()
            }
        }

        return http {
            addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            authorizeExchange {
                authorize(pathMatchers(HttpMethod.OPTIONS, "/**"), permitAll)
                authorize(pathMatchers("/api/users", "/api/users/login"), permitAll)
                authorize()
            }
            cors {
                configurationSource = corsConfiguration {
                    addAllowedOriginPattern("/**")
                    addAllowedOriginPattern("*")
                    addAllowedMethod("*")
                    addAllowedHeader("*")
                }
            }
            exceptionHandling {
                authenticationEntryPoint = ServerAuthenticationEntryPoint { swe, _ ->
                    fromRunnable {
                        swe.response.statusCode = HttpStatus.UNAUTHORIZED
                    }
                }
                accessDeniedHandler = ServerAccessDeniedHandler { swe, _ ->
                    fromRunnable {
                        swe.response.statusCode = HttpStatus.FORBIDDEN
                    }
                }
            }
        }
    }
}