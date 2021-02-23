package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.util.corsConfiguration
import com.marks0mmers.budgetcreator.util.getBearerAuth
import com.marks0mmers.budgetcreator.util.mono
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers
import reactor.core.publisher.Mono.empty
import reactor.kotlin.core.publisher.toMono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(val authenticationManager: AuthenticationManager) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity) = http {
        addFilterAt(AuthenticationWebFilter(authenticationManager).apply {
            setServerAuthenticationConverter { swe ->
                swe?.request?.headers?.getBearerAuth()?.let { authToken ->
                    UsernamePasswordAuthenticationToken(authToken, authToken).toMono()
                } ?: empty()
            }
        }, SecurityWebFiltersOrder.AUTHENTICATION)
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
                mono { swe.response.statusCode = HttpStatus.UNAUTHORIZED }
            }
            accessDeniedHandler = ServerAccessDeniedHandler { swe, _ ->
                mono { swe.response.statusCode = HttpStatus.FORBIDDEN }
            }
        }
    }
}