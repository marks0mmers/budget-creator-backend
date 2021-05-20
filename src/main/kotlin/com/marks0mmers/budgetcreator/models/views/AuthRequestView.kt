package com.marks0mmers.budgetcreator.models.views

/**
 * The JSON submission view for logging into the application
 *
 * @property username The username to log into
 * @property password The password to use to login
 */
data class AuthRequestView(
    val username: String,
    val password: String
)