package com.marks0mmers.budgetcreator.models.persistent

import org.springframework.security.core.GrantedAuthority

/**
 * The roles constants for a user
 *
 * @author Mark Sommers
 */
enum class Role : GrantedAuthority {
    /** The default role, which defines common user functionality */
    ROLE_USER,
    /** The admin role which allows for administrative tasks */
    ROLE_ADMIN;

    /**
     * Gets the authority which returns the string name of the role
     * @see GrantedAuthority.getAuthority
     */
    override fun getAuthority() = name
}
