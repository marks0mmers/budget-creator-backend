package com.marks0mmers.budgetcreator.util

import org.springframework.http.HttpHeaders
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
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