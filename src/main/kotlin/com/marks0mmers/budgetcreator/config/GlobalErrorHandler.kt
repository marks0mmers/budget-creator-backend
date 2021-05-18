package com.marks0mmers.budgetcreator.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.marks0mmers.budgetcreator.util.BudgetCreatorException
import com.marks0mmers.budgetcreator.util.toByteArray
import io.jsonwebtoken.JwtException
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebInputException
import reactor.kotlin.core.publisher.toMono

/**
 * A handler that handles all exceptions thrown by the application
 * It takes the exception, and returns the useful information back as a Response
 *
 * @author Mark Sommers
 * @see Ordered
 * @see ErrorWebExceptionHandler
 */
class GlobalErrorHandler : Ordered by Ordered({ -2 }),
    ErrorWebExceptionHandler by ErrorWebExceptionHandler({ exchange, ex ->
        val buffer = exchange.response.bufferFactory()
        val jsonEncoder = jacksonObjectMapper()
        exchange.response.statusCode = when (ex) {
            is BudgetCreatorException -> ex.status
            is ServerWebInputException -> ex.status
            is ResponseStatusException -> ex.status
            is JwtException -> UNAUTHORIZED
            else -> INTERNAL_SERVER_ERROR
        }
        val bufferJson = when (ex) {
            is BudgetCreatorException -> jsonEncoder.writeValueAsBytes(ex)
            is ServerWebInputException -> jsonEncoder.createObjectNode()
                .put("message", ex.reason)
                .put("cause", ex.cause.toString())
                .toByteArray()
            else -> jsonEncoder.createObjectNode()
                .put("message", ex.message)
                .put("cause", ex.cause.toString())
                .toByteArray()
        }
        exchange.response.headers.contentType = APPLICATION_JSON
        exchange.response.writeWith(buffer.wrap(bufferJson).toMono())
    })
