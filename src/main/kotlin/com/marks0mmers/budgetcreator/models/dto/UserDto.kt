package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.persistent.Role
import com.marks0mmers.budgetcreator.models.persistent.User

/**
 * The DTO that represents a User
 *
 * @property id The id of the user
 * @property username The username of the user
 * @property firstName The first name of the user
 * @property lastName The last name of the user
 * @property enabled Whether the user is enabled
 * @property roles The list of roles for the user
 * @property token A JWT Token associated with a logged in user
 */
data class UserDto(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    var enabled: Boolean,
    var roles: List<Role>,
    var token: String? = null
) {
    /**
     * Creates a DTO based on the user entity
     * @param user The user entity to create from
     */
    constructor(user: User) : this(
        user.id.value,
        user.username,
        user.firstName,
        user.lastName,
        user.enabled,
        user.roles.map { it.role }
    )
}
