package com.marks0mmers.budgetcreator.models.views

/**
 * The JSON submission for a user object
 *
 * @property username The username of the user
 * @property password The password for the user
 * @property firstName The first name of the user
 * @property lastName The last name of the user
 */
data class CreateUserView(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String
)