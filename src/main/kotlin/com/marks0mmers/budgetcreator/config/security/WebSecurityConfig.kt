package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.util.*
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurityDsl
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers
import reactor.core.publisher.Mono.empty
import reactor.kotlin.core.publisher.toMono

/**
 * The security configuration for the application. Sets up the Bearer auth, turns off CSRF, FormLogin, httpBasic login,
 * and configures CORS and Exception Handling
 *
 * @see ServerHttpSecurityDsl
 * @author Mark Sommers
 *
 * @constructor
 * Builds the Security Config from injected dependencies
 *
 * @param authenticationManager The authentication manager to use
 * @param http The http security object to configure
 */
class WebSecurityConfig(
    authenticationManager: AuthenticationManager,
    http: ServerHttpSecurity
) : SecurityWebFilterChain by http({
    authenticationTokenFilter(authenticationManager) { swe ->
        swe?.request?.headers?.bearerAuth?.let { authToken ->
            UsernamePasswordAuthenticationToken(authToken, authToken).toMono()
        } ?: empty()
    }
    csrf { disable() }
    formLogin { disable() }
    httpBasic { disable() }
    authorizeExchange {
        authorize(pathMatchers(HttpMethod.OPTIONS, "/**"), permitAll)
        authorize(pathMatchers(HttpMethod.POST,"/api/users", "/api/users/login"), permitAll)
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
})