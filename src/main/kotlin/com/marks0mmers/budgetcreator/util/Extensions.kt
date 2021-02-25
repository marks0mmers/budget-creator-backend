@file:Suppress("FunctionName")

package com.marks0mmers.budgetcreator.util

import org.springframework.http.HttpHeaders
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

fun corsConfiguration(builder: CorsConfiguration.() -> Unit): CorsConfigurationSource {
    return CorsConfigurationSource {
        CorsConfiguration().apply(builder)
    }
}

fun HttpHeaders.getAuthorization(): String? = getFirst(HttpHeaders.AUTHORIZATION)

fun HttpHeaders.getBearerAuth(): String? =
    if (getAuthorization()?.startsWith("Bearer ") == true) getAuthorization()?.substring(7) else null

fun mono(run: () -> Unit): Mono<Void> {
    return Mono.fromRunnable<Unit>(run).then()
}

fun CoRouterFunctionDsl.GET(f: suspend (ServerRequest) -> ServerResponse) = GET("", f)
//fun CoRouterFunctionDsl.HEAD(f: suspend (ServerRequest) -> ServerResponse) = HEAD("", f)
fun CoRouterFunctionDsl.POST(f: suspend (ServerRequest) -> ServerResponse) = POST("", f)
fun CoRouterFunctionDsl.PUT(f: suspend (ServerRequest) -> ServerResponse) = PUT("", f)
//fun CoRouterFunctionDsl.PATCH(f: suspend (ServerRequest) -> ServerResponse) = PATCH("", f)
fun CoRouterFunctionDsl.DELETE(f: suspend (ServerRequest) -> ServerResponse) = DELETE("", f)
//fun CoRouterFunctionDsl.OPTIONS(f: suspend (ServerRequest) -> ServerResponse) = OPTIONS("", f)