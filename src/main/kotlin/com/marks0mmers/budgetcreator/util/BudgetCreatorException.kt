package com.marks0mmers.budgetcreator.util

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

class BudgetCreatorException(override val message: String, val status: HttpStatus) : Exception(message)

fun fail(message: String): Nothing {
    throw BudgetCreatorException(message, HttpStatus.BAD_REQUEST)
}

fun fail(message: String, status: HttpStatus): Nothing {
    throw BudgetCreatorException(message, status)
}