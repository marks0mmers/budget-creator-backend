package com.marks0mmers.budgetcreator.util

import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource

fun corsConfiguration(builder: CorsConfiguration.() -> Unit): CorsConfigurationSource {
    return CorsConfigurationSource {
        CorsConfiguration().apply(builder)
    }
}