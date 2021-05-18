package com.marks0mmers.budgetcreator.models.dto

import com.marks0mmers.budgetcreator.models.constants.Role
import com.marks0mmers.budgetcreator.models.persistent.User

data class UserDto(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    var enabled: Boolean,
    var roles: List<Role>,
    var token: String? = null
) {
    constructor(user: User) : this(
        user.id.value,
        user.username,
        user.firstName,
        user.lastName,
        user.enabled,
        user.roles.map { it.role }
    )
}
