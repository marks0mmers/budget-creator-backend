@file:Suppress("FunctionName")

package com.marks0mmers.budgetcreator.util

import com.fasterxml.jackson.databind.node.ObjectNode
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.ServerHttpSecurityDsl
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.nio.charset.Charset
import java.util.*

/**
 * A small DSL object that allows the CORS section of spring security to look cleaner
 *
 * @param builder The [CorsConfiguration] object used to build the source
 * @return The built CORS config source
 */
fun corsConfiguration(builder: CorsConfiguration.() -> Unit): CorsConfigurationSource {
    return CorsConfigurationSource {
        CorsConfiguration().apply(builder)
    }
}

/**
 * Shorthand to add a filter at the [authentication][AUTHENTICATION] spot in the web filter chain.
 * It uses [AuthenticationWebFilter] and sets the converter to the provided one.
 *
 * @param authenticationManager The authentication manager to pass to
 * @param serverAuthenticationConverter The converter to be added to the authentication web filter
 */
fun ServerHttpSecurityDsl.authenticationTokenFilter(authenticationManager: ReactiveAuthenticationManager, serverAuthenticationConverter: ServerAuthenticationConverter) {
    addFilterAt(AuthenticationWebFilter(authenticationManager).apply {
        setServerAuthenticationConverter(serverAuthenticationConverter)
    }, AUTHENTICATION)
}

/**
 * Converts an Instant to a Java Date object
 */
val Instant.javaDate: Date
    get() = Date.from(toJavaInstant())

/**
 * Converts a Java Date to an Instant object
 */
val Date.instant: Instant
    get() = Instant.fromEpochMilliseconds(toInstant().toEpochMilli())

/**
 * Gets the first header value of the "Authorization" key
 *
 * @see [HttpHeaders.getFirst]
 */
val HttpHeaders.authorization: String?
    get() = this.getFirst(HttpHeaders.AUTHORIZATION)

/**
 * Gets the bearer token from the "Authorization" header value
 * @see [authorization]
 */
val HttpHeaders.bearerAuth: String?
    get() = if (authorization?.startsWith("Bearer ") == true) authorization?.substring(7) else null

/**
 * Helper function to make a Void Mono from a Runnable
 *
 * @param run The runnable to run
 * @return The Void Mono
 */
fun mono(run: Runnable): Mono<Void> {
    return Mono.fromRunnable<Unit>(run).then()
}

/**
 * Helper function to go straight from ObjectNode to ByteArray
 *
 * @param charset The charset to convert the string to the ByteArray
 * @return The converted Byte Array
 */
fun ObjectNode.toByteArray(charset: Charset = Charsets.UTF_8) = toString().toByteArray(charset)

/**
 * A helper function to define a GET request without an additional URL
 *
 * @param f The handler function for the route
 */
fun CoRouterFunctionDsl.GET(f: suspend (ServerRequest) -> ServerResponse) = GET("", f)
//fun CoRouterFunctionDsl.HEAD(f: suspend (ServerRequest) -> ServerResponse) = HEAD("", f)
/**
 * A helper function to define a POST request without an additional URL
 *
 * @param f The handler function for the route
 */
fun CoRouterFunctionDsl.POST(f: suspend (ServerRequest) -> ServerResponse) = POST("", f)
//fun CoRouterFunctionDsl.PUT(f: suspend (ServerRequest) -> ServerResponse) = PUT("", f)
//fun CoRouterFunctionDsl.PATCH(f: suspend (ServerRequest) -> ServerResponse) = PATCH("", f)
//fun CoRouterFunctionDsl.DELETE(f: suspend (ServerRequest) -> ServerResponse) = DELETE("", f)
//fun CoRouterFunctionDsl.OPTIONS(f: suspend (ServerRequest) -> ServerResponse) = OPTIONS("", f)