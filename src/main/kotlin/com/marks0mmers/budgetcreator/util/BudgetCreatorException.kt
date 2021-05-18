package com.marks0mmers.budgetcreator.util

import org.springframework.http.HttpStatus

/**
 * A general exception for the Budget Creator errors
 *
 * @property message The message for the exception
 * @property status an optional HTTP status code to return
 */
class BudgetCreatorException(override val message: String, val status: HttpStatus) : Exception(message)

/**
 * A helper function to throw a BudgetCreatorException
 *
 * @param message The message for the exception
 * @throws BudgetCreatorException This will always throw
 */
fun fail(message: String): Nothing {
    throw BudgetCreatorException(message, HttpStatus.BAD_REQUEST)
}

/**
 * A helper function to throw a BudgetCreatorException with a HTTP status code
 *
 * @param message The message for the exception
 * @param status The [HttpStatus] code
 * @throws BudgetCreatorException This will always throw
 */
fun fail(message: String, status: HttpStatus): Nothing {
    throw BudgetCreatorException(message, status)
}